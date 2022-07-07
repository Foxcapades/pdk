GIT_REV  = $(shell git describe --tags)

.PHONY: default
default:
	@echo "What are you doing?"


gen-tag-docs:
	@# Require that we are on the tag revision
	@git describe --tags --exact-match || (echo "\n\nMust be on a tag to run this make target.\n\n"; exit 1)

	@# Generate the docs
	@gradle dokkaHtml

	@# Switch to the docs branch
	@git checkout docs

	@# Remove any old versions for this tag?
	@rm -rf dokka/$(GIT_REV)

	@# Move the generated docs into position
	@mv build/docs/dokka dokka/$(GIT_REV)

	@# Stop here, we need to manually update the readme and generate a new index
	@# html file before we can commit and push.
	@echo "Now perform the following manual tasks:"
	@echo "  - Update the readme.adoc file with a link the generated docs"
	@echo "  - run 'make index.html' to regenerate the index file"
	@echo "  - commit the changes and push up to github"

gen-latest-docs:
	@gradle dokkaHtml
	@git checkout docs
	@rm -rf dokka/latest
	@mv build/docs/dokka dokka/latest
	@git add dokka/latest
	@git commit -m 'update latest docs'
	@git push
	@git checkout main

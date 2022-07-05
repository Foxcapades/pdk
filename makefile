PKG_DIR  = "src/main/kotlin/io/foxcapades/lib/pdk"
TYPES    = Boolean Short Int Long Float Double UByte UShort UInt ULong Char
TPL_ROOT = "$(PKG_DIR)/ByteDeque.kt"
GIT_REV  = $(shell git describe --tags)

.PHONY: default
default:
	@echo "What are you doing?"

.PHONY: clone-deques
clone-deques:
	@for type in $(TYPES) ; do \
  		rm -f $(PKG_DIR)/$${type}Deque.kt ; \
  		sed "s/Byte/$${type}/g" $(TPL_ROOT) > $(PKG_DIR)/$${type}Deque.kt ; \
	done

gen-tag-docs:
	@# Require that we are on the tag revision
	@git describe --tags --exact-match

	@# Generate the docs
	@gradle dokkaHtml

	@# Switch to the docs branch
	@git checkout docs

	@ls

gen-latest-docs:
	@gradle dokkaHtml
	@git checkout docs
	@rm -rf dokka/latest
	@mv build/docs/dokka dokka/latest
	@git add dokka/latest
	@git commit -m 'update latest docs'
	@git push
	@git checkout main
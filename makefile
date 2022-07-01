PKG_DIR  = "src/main/kotlin/io/foxcapades/lib/pdk"
TYPES    = Boolean Short Int Long Float Double UByte UShort UInt ULong Char
TPL_ROOT = "$(PKG_DIR)/ByteDeque.kt"

.PHONY: default
default:
	@echo "What are you doing?"

.PHONY: clone-deques
clone-deques:
	@for type in $(TYPES) ; do \
  		rm -f $(PKG_DIR)/$${type}Deque.kt ; \
  		sed "s/Byte/$${type}/g" $(TPL_ROOT) > $(PKG_DIR)/$${type}Deque.kt ; \
	done
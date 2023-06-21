BUILD_DIR = ./build

export PATH := $(PATH):$(abspath ./utils)

test:
	mill -i -j 0 __.test

verilog:
	mkdir -p $(BUILD_DIR)
	mill -i -j 0 __.test.runMain Elaborate -td $(BUILD_DIR)

help:
	mill -i __.test.runMain Elaborate --help

compile:
	mill -i -j 0 __.compile

bsp:
	mill -i -j 0 mill.bsp.BSP/install

reformat:
	mill -i -j 0 __.reformat

checkformat:
	mill -i -j 0 __.checkFormat

doc:
	mill -j 0 show __.docJar

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: test verilog help compile bsp reformat checkformat doc clean

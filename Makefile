BUILD_DIR = ./build

export PATH := $(PATH):$(abspath ./utils)

test:
	mkdir -p $(BUILD_DIR)
	utils/mill -i -j 0 __.test

verilog:
	mkdir -p $(BUILD_DIR)
	utils/mill -i -j 0 __.test.runMain Elaborate -td $(BUILD_DIR)

help:
	utils/mill -i __.test.runMain Elaborate --help

compile:
	utils/mill -i -j 0 __.compile

bsp:
	utils/mill -i -j 0 mill.bsp.BSP/install

reformat:
	utils/mill -i -j 0 __.reformat

checkformat:
	utils/mill -i -j 0 __.checkFormat

doc:
	utils/mill -j 0 show __.docJar

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: test verilog help compile bsp reformat checkformat doc clean

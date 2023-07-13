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

build_docker:
	docker build -f docker/Dockerfile -t chisel-playground:latest .

run_docker: build_docker
	docker run -it --rm -v $(shell pwd):/workspace/$(shell basename $$(pwd)) --workdir /workspace/$(shell basename $$(pwd)) chisel-playground:latest

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: test verilog help compile bsp reformat checkformat doc build_docker run_docker clean

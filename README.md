# Chisel ChipWare

This is a Chisel wrapper to instance ChipWare IPs.

**Note: This repo does NOT include any implementation details of the Cadence ChipWare IPs!!!**

## Getting Started

### Add this repo as a submodule in your Chisel Project

```bash
git submodule add git@github.com:SingularityKChen/chisel-chipware.git
```

Also remember to add the dependency in your `build.sbt` or `build.sc`.

### Call the ChipWare IPs in your Chisel Project

1. import the ChipWare IPs in your Chisel project

```scala
import chipware._
```

2. create a ChipWare IP instance and wrapper it with `Module()`. 
For example, if you want to use the `CW_add` IP, you can do the following:

```scala
protected val U1: CW_add = Module(new CW_add(wA))
U1.io.A  := io.A
U1.io.B  := io.B
U1.io.CI := io.CI
io.Z     := U1.io.Z
io.CO    := U1.io.CO
```

3. add the black box path for verification

```scala
U1.addPath(blackBoxPath)
```

You may reference [the demo file](chipware/test/src/DemoTest.scala) for more details.


## Docker

### Build the Docker Image

```bash
make build_docker
```

### Run the Docker Image

```bash
make run_docker
```

## To develop this repo

### Install Mill

First, install mill by referring to the documentation [here](https://com-lihaoyi.github.io/mill).

### Build bsp and compile the project

```bash
make bsp compile
```

### Generate the ScalaDoc

```bash
make doc
```

### Run the tests

```bash
make test
```

# Thanks

This repo uses the template from [chisel-playground](https://github.com/OpenXiangShan/chisel-playground).

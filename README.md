# Chisel ChipWare

This is a Chisel wrapper to instance ChipWare IPs.

**Note: This repo does NOT include any implementation details of the Cadence ChipWare IPs!!!**

## Getting Started

### Add this repo as a submodule in your Chisel Project

```bash
git submodule add git@github.com:SingularityKChen/chisel-chipware.git
```

### Call the ChipWare IPs in your Chisel Project

```scala

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
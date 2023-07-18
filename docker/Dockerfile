# We're using a multistage Docker build here in order to allow us to release a self-verifying
# Docker image when built on the official Docker infrastructure.
# They require us to verify the source integrity in some way while making sure that this is a
# reproducible build.
# See https://github.com/docker-library/official-images#image-build
# In order to achieve this, we externally host the rootfs archives and their checksums and then
# just download and verify it in the first stage of this Dockerfile.
# The second stage is for actually configuring the system a little bit.
# Some templating is done in order to allow us to easily build different configurations and to
# allow us to automate the releaes process.
FROM alpine:3.18 AS verify

RUN apk add --no-cache curl tar zstd

# https://gitlab.archlinux.org/archlinux/archlinux-docker/-/releases/v20230709.0.163418
RUN ROOTFS="$(curl -sOJL -w "%{filename_effective}" "https://gitlab.archlinux.org/archlinux/archlinux-docker/-/package_files/4840/download")" && \
    echo "9d301b49c4ef3174ffbafc781b0cde2741c9b0a2f5451d65abf28d77a281aa3f  base-20230709.0.163418.tar.zst" > /tmp/rootfs.tar.sha256 && \
    cat /tmp/rootfs.tar.sha256 && \
    sha256sum -c /tmp/rootfs.tar.sha256 && \
    mkdir /rootfs && \
    tar -C /rootfs --extract --file "${ROOTFS}"

FROM scratch AS root
COPY --from=verify /rootfs/ /

RUN ldconfig && \
    sed -i '/BUILD_ID/a VERSION_ID=20230709.0.163418' /etc/os-release

ENV LANG=C.UTF-8

RUN pacman -Syu --noconfirm make parallel wget cmake ninja dtc verilator git llvm clang lld protobuf antlr4 python autoconf automake

RUN pacman -Syu --noconfirm vim which

# Download and install mill
RUN curl -L https://github.com/com-lihaoyi/mill/releases/download/0.11.1/0.11.1 > mill && \
    chmod +x mill && \
    mv mill /usr/bin/mill

# ivy development dependencies
RUN git clone https://github.com/SingularityKChen/chisel-chipware.git && \
    cd chisel-chipware && \
    mill -i -j 0 __.compile && \
    mill -i -j 0 __.reformat && \
    cd .. && \
    rm -rf chisel-chipware

CMD ["/usr/bin/bash"]
FROM archlinux:latest AS root

ENV LANG=C.UTF-8

RUN pacman -Syu --noconfirm make parallel wget cmake ninja verilator git llvm clang python autoconf automake

RUN pacman -Syu --noconfirm vim which

# Install GraalVM Ref: https://github.com/chipsalliance/chisel/blob/86b2a7d3f7807ca0d1d8a725bd87ee4be1f61007/.devcontainer/Dockerfile#L32-L40
# This downloads all relevant GraalVM architectures at once, mostly because $TARGETARCH values don't map exactly to the release URLs. Since we're optimizing for developer experience here and not image size, this is OK
# GraalVM release links can be found here: https://github.com/graalvm/graalvm-ce-builds/releases
ENV JAVA_VERSION=java11
#ADD https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.1/graalvm-ce-$JAVA_VERSION-linux-aarch64-22.3.1.tar.gz /graalvm/tarballs/arm64.tar.gz
ADD https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.1/graalvm-ce-$JAVA_VERSION-linux-amd64-22.3.1.tar.gz /graalvm/tarballs/amd64.tar.gz
#RUN tar -xzf /graalvm/tarballs/$TARGETARCH.tar.gz -C /graalvm --strip-components=1
RUN tar -xzf /graalvm/tarballs/amd64.tar.gz -C /graalvm --strip-components=1
ENV JAVA_HOME=/graalvm
ENV PATH=$JAVA_HOME/bin/:$PATH

# Download and install mill
RUN curl -L https://github.com/com-lihaoyi/mill/releases/download/0.11.5/0.11.5 > mill && \
    chmod +x mill && \
    mv mill /usr/bin/mill

# ivy development dependencies
RUN cd /tmp && \
    git clone https://github.com/SingularityKChen/chisel-chipware.git
RUN cd /tmp/chisel-chipware && \
    mill -i -j 0 __.compile && \
    mill -i -j 0 __.reformat && \
    cd .. && \
    rm -rf chisel-chipware

CMD ["/usr/bin/bash"]

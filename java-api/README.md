
Here's what we did on out x86_64 box to build 32bit Java, and run the repo
```bash
sudo docker run -it almalinux/9-base:9 /bin/bash
linux32 dnf -y update
dnf install -y java-21-openjdk-devel.x86_64
dnf config-manager --set-enabled crb
dnf install -y almalinux-release-devel
dnf install -y glibc-devel.i686 libgcc.i686 libstdc++-devel.i686
dnf groupinstall "Development Tools"
// this may be superseded by gcc-toolset-13-gcc below
dnf install -y \
    glibc-devel.i686 libgcc.i686 libstdc++-devel.i686 \
    libX11-devel.i686 libXext-devel.i686 libXrender-devel.i686 \
    libXrandr-devel.i686 libXtst-devel.i686 libXt-devel.i686 \
    cups-devel.i686 alsa-lib-devel.i686 fontconfig-devel.i686 \
    libffi-devel.i686 zlib-devel.i686
git clone https://github.com/openjdk/jdk22u
cd jdk22u
dnf install -y gcc-toolset-13-gcc gcc-toolset-13-gcc-c++
bash configure --with-target-bits=32
// fake a library for linking
mkdir -p /opt/rh/gcc-toolset-13/root/usr/lib/gcc/i686-redhat-linux/13
ar rcs /opt/rh/gcc-toolset-13/root/usr/lib/gcc/i686-redhat-linux/13/libstdc++_nonshared.a
cp /usr/include/freetype2/freetype/config/ftconfig-64.h /usr/include/freetype2/freetype/config/ftconfig-32.h
ln -s /usr/lib/libfreetype.so.6 /usr/lib/libfreetype.so
make
sdk install java 22-local32 /jdk22u/build/linux-x86-server-release/jdk
sdk install gradle 9.8.2
mkdir /home/alan
cd /home/alan
git clone https://github.com/evolvedbinary/jextract-portability
gradle wrapper
dnf install emacs
sdk install jextract
```

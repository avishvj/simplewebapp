FROM openjdk:11-jdk

WORKDIR /source

RUN apt-get -y update

RUN apt-get -y install haskell-platform

RUN apt-get -y install -o Acquire:Retries=10 --no-install-recommends \
    texlive-latex-base \
    texlive-xetex latex-xcolor \
    texlive-math-extra \
    texlive-latex-extra \
    texlive-fonts-extra \
    texlive-bibtex-extra \
    fontconfig \
    lmodern

RUN apt-get -y install pandoc

RUN apt-get -y install maven --no-install-recommends

WORKDIR /app

COPY . /app

RUN mvn package

CMD ["sh", "target/bin/simplewebapp"]
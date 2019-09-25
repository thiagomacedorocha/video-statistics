FROM adoptopenjdk/openjdk11:ppc64le-ubi-minimal-jre11u-nightly

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/local/javapkg/entrypoint.jar"]

ADD target/entrypoint.jar /usr/local/javapkg/entrypoint.jar
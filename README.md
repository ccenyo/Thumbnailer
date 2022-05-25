# Thumbnailer

Have you ever think of making thumbnail from video automatically ?
This library helps you doing that. 
You can generate thumbnail from a video. It's pretty flexible, you can choose the number of frames from the video to use, the number of columns in the thumbnail and number of lines.

##Prerequisites

This project is written in java 17. you can only use it in a project that uses java 17 and above.
if you want to use it in any environment, make sure you have the right JVM installed on the machine.


## How to use it

add the maven repository to your pom.xml

```java

```

##Basic usage


```java
       var file = new VideoThumbnail(new File("path/to/file.mp4"), "/destination/path")
        .automaticDimensions()
        .generate();
```

![portrait image](./images/1653463354709.jpeg?raw=true "Title")

```java
        var file = new VideoThumbnail(new File("path/to/file.mp4"), "/destination/path")
        .setNumberOfFrame(4)
        .setImageDivisor(new ImageDivisor(1,4))
        .generate();
```

![portrait image](./images/1653463529590.jpeg?raw=true "Title")

## Built With
* [Java SDK 17](https://www.oracle.com/technetwork/java/javase/downloads/jdk17-downloads-2133151.html) -  Java™ Platform
* [Maven](https://maven.apache.org/) - Dependency Management

## Author
* **Cenyo Medewou** - [medewou@gmail.com](mailto:medewou@gmail.com).

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details                                                                        


package com.diffreviewer.controller;

import java.io.*;

public class HtmlReaderWriter {

    public static final String REQUEST_BEGIN = "<div id=\"diff\">";

    public static final String REQUEST_END = "</body>";

    public static final String DIFF_REV_BEGIN = "<!DOCTYPE HTML>\n" +
            "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
            "<head>\n" +
            "    <title>Diff</title>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<header th:insert=\"blocks/header :: header\"></header>\n";

    public static final String DIFF_REV_END = "</body>\n";

    private String pathToDiffRevFile = "./src/main/resources/templates/diff_rev.html";

    public String getBody(String path) throws IOException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        String next = "";
        for(int i = 0; i < 65; i++) {
            randomAccessFile.readLine();
        }
        while (!next.contains(REQUEST_BEGIN)) {
            next = randomAccessFile.readLine();
        }
        while (!next.contains(REQUEST_END)) {
            stringBuilder.append(next).append("\n");
            next = randomAccessFile.readLine();
        }
        return stringBuilder.toString();
    }

    public void writeToDiffRev(String body) {
        File file = new File(pathToDiffRevFile);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(DIFF_REV_BEGIN);
            writer.write(body);
            writer.write(DIFF_REV_END);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getPathToDiffRevFile() {
        return pathToDiffRevFile;
    }

    public void setPathToDiffRevFile(String pathToDiffRevFile) {
        this.pathToDiffRevFile = pathToDiffRevFile;
    }

    public static void main(String[] args) throws IOException {
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();
        htmlReaderWriter.writeToDiffRev(htmlReaderWriter.getBody("./output-file.html"));
    }
}

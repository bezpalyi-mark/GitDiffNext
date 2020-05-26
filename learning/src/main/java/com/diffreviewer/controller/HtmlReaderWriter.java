package com.diffreviewer.controller;

import java.io.*;

public class HtmlReaderWriter {

    public static final String REQUEST_BEGIN = "<div id=\"diff\">";

    public static final String REQUEST_END = "</body>";

    public static final String DIFF_REV_BEGIN = "<div th:fragment = \"diff_rev\">\n";

    public static final String DIFF_REV_END = "</div>\n";

    private String pathToDiffRevFile = "./src/main/resources/templates/blocks/diff_rev.html";

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
        htmlReaderWriter.writeToDiffRev(htmlReaderWriter.getBody("./src/main/resources/static/diffFilesHtml/mark.html"));
    }
}

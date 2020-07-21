package com.diffreviewer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HtmlReaderWriter {

    public static final Logger LOGGER = LoggerFactory.getLogger(HtmlReaderWriter.class);

    public static final String REQUEST_BEGIN = "<div id=\"diff\">";

    public static final String REQUEST_END = "</body>";

    public static final String DIFF_REV_BEGIN = "<div th:fragment = \"diff_rev\">\n";

    public static final String DIFF_REV_END = "</div>\n";

    private String pathToDiffRevFile = "./src/main/resources/templates/blocks/diff_rev.html";

    public static final String HEAD = "<head>\n" +
            "    <title>Changes review</title>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.9.0/styles/github.min.css\">\n" +
            "    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
            "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
            "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
            "    <style>\n" +
            "        .d2h-wrapper{text-align:left}.d2h-file-header{height:35px;padding:5px 10px;border-bottom:1px solid #d8d8d8;background-color:#f7f7f7}.d2h-file-stats{display:-webkit-box;display:-ms-flexbox;display:flex;margin-left:auto;font-size:14px}.d2h-lines-added{text-align:right;border:1px solid #b4e2b4;border-radius:5px 0 0 5px;color:#399839;padding:2px;vertical-align:middle}.d2h-lines-deleted{text-align:left;border:1px solid #e9aeae;border-radius:0 5px 5px 0;color:#c33;padding:2px;vertical-align:middle;margin-left:1px}.d2h-file-name-wrapper{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-align:center;-ms-flex-align:center;align-items:center;width:100%;font-family:Source Sans Pro,Helvetica Neue,Helvetica,Arial,sans-serif;font-size:15px}.d2h-file-name{white-space:nowrap;text-overflow:ellipsis;overflow-x:hidden}.d2h-file-wrapper{border:1px solid #ddd;border-radius:3px;margin-bottom:1em}.d2h-diff-table{width:100%;border-collapse:collapse;font-family:Menlo,Consolas,monospace;font-size:13px}.d2h-files-diff{display:block;width:100%;height:100%}.d2h-file-diff{overflow-y:hidden}.d2h-file-side-diff{display:inline-block;overflow-x:scroll;overflow-y:hidden;width:50%;margin-right:-4px;margin-bottom:-8px}.d2h-code-line{padding:0 8em}.d2h-code-line,.d2h-code-side-line{display:inline-block;white-space:nowrap}.d2h-code-side-line{padding:0 4.5em}.d2h-code-line del,.d2h-code-side-line del{display:inline-block;margin-top:-1px;text-decoration:none;background-color:#ffb6ba;border-radius:.2em}.d2h-code-line ins,.d2h-code-side-line ins{display:inline-block;margin-top:-1px;text-decoration:none;background-color:#97f295;border-radius:.2em;text-align:left}.d2h-code-line-ctn,.d2h-code-line-prefix{display:inline;background:none;padding:0;word-wrap:normal;white-space:pre}.line-num1{float:left}.line-num1,.line-num2{-webkit-box-sizing:border-box;box-sizing:border-box;width:3.5em;overflow:hidden;text-overflow:ellipsis;padding:0 .5em}.line-num2{float:right}.d2h-code-linenumber{-webkit-box-sizing:border-box;box-sizing:border-box;width:7.5em;position:absolute;display:inline-block;background-color:#fff;color:rgba(0,0,0,.3);text-align:right;border:solid #eee;border-width:0 1px;cursor:pointer}.d2h-code-linenumber:after{content:\\\"\\\\200b\\\"}.d2h-code-side-linenumber{position:absolute;display:inline-block;-webkit-box-sizing:border-box;box-sizing:border-box;width:4em;background-color:#fff;color:rgba(0,0,0,.3);text-align:right;border:solid #eee;border-width:0 1px;cursor:pointer;overflow:hidden;text-overflow:ellipsis;padding:0 .5em}.d2h-code-side-linenumber:after{content:\\\"\\\\200b\\\"}.d2h-code-side-emptyplaceholder,.d2h-emptyplaceholder{background-color:#f1f1f1;border-color:#e1e1e1}.d2h-code-line-prefix,.d2h-code-linenumber,.d2h-code-side-linenumber,.d2h-emptyplaceholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.d2h-code-linenumber,.d2h-code-side-linenumber{direction:rtl}.d2h-del{background-color:#fee8e9;border-color:#e9aeae}.d2h-ins{background-color:#dfd;border-color:#b4e2b4}.d2h-info{background-color:#f8fafd;color:rgba(0,0,0,.3);border-color:#d5e4f2}.d2h-file-diff .d2h-del.d2h-change{background-color:#fdf2d0}.d2h-file-diff .d2h-ins.d2h-change{background-color:#ded}.d2h-file-list-wrapper{margin-bottom:10px}.d2h-file-list-wrapper a{text-decoration:none;color:#3572b0}.d2h-file-list-wrapper a:visited{color:#3572b0}.d2h-file-list-header{text-align:left}.d2h-file-list-title{font-weight:700}.d2h-file-list-line{display:-webkit-box;display:-ms-flexbox;display:flex;text-align:left}.d2h-file-list{display:block;list-style:none;padding:0;margin:0}.d2h-file-list>li{border-bottom:1px solid #ddd;padding:5px 10px;margin:0}.d2h-file-list>li:last-child{border-bottom:none}.d2h-file-switch{display:none;font-size:10px;cursor:pointer}.d2h-icon{vertical-align:middle;margin-right:10px;fill:currentColor}.d2h-deleted{color:#c33}.d2h-added{color:#399839}.d2h-changed{color:#d0b44c}.d2h-moved{color:#3572b0}.d2h-tag{display:-webkit-box;display:-ms-flexbox;display:flex;font-size:10px;margin-left:5px;padding:0 2px;background-color:#fff}.d2h-deleted-tag{border:1px solid #c33}.d2h-added-tag{border:1px solid #399839}.d2h-changed-tag{border:1px solid #d0b44c}.d2h-moved-tag{border:1px solid #3572b0}\\n\" +\n" +
            "    </style>\n" +
            "\n" +
            "</head>";


    public static final String HEADER = "<header>" +
            "<div th:fragment = \"header\">\n" +
            "    <div class=\"d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom shadow-sm\">\n" +
            "        <h5 class=\"my-0 mr-md-auto font-weight-normal\">IT-Project</h5>\n" +
            "        <nav class=\"my-2 my-md-0 mr-md-3\">\n" +
            "            <a class=\"p-2 text-dark\" href=\"/main-tree\">Tree</a>\n" +
            "            <a class=\"p-2 text-dark\" href=\"/profile\">Profile</a>\n" +
            "        </nav>\n" +
            "    </div>\n" +
            "</div>" +
            "<header>";

    public String getBody(String path) throws IOException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
            String next = "";
            for (int i = 0; i < 65; i++) {
                randomAccessFile.readLine();
            }
            while (!next.contains(REQUEST_BEGIN)) {
                next = randomAccessFile.readLine();
            }
            while (!next.contains(REQUEST_END)) {
                stringBuilder.append(next).append("\n");
                next = randomAccessFile.readLine();
            }
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
            LOGGER.error(e.getMessage());
        }
    }

    public String getPathToDiffRevFile() {
        return pathToDiffRevFile;
    }

    public void setPathToDiffRevFile(String pathToDiffRevFile) {
        this.pathToDiffRevFile = pathToDiffRevFile;
    }

    /**
     * Just for example
     *
     * @param args cli.
     * @throws IOException io error.
     */
    public static void main(String[] args) throws IOException {
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();
        htmlReaderWriter.writeToDiffRev(htmlReaderWriter.getBody("./src/main/resources/static/diffFilesHtml/mark.html"));
    }
}

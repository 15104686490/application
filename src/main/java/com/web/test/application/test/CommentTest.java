package com.web.test.application.test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFComment;
import org.apache.poi.xwpf.usermodel.*;


public class CommentTest {
    /*public static void main(String[] args) {
        try {
            // 1. 打开 Word 文档
            XWPFDocument doc = new XWPFDocument(new FileInputStream("example.docx"));

            // 2. 查找要添加批注的文本
            String searchText = "要查找的文本";
            XWPFParagraph foundParagraph = null;
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(searchText)) {
                    foundParagraph = p;
                    break;
                }
            }

            // 3. 添加批注
            if (foundParagraph != null) {
                XWPFComment comment = foundParagraph.createComment();
                comment.setAuthor("作者");
                comment.setText("批注内容");
            }

            // 4. 保存 Word 文档
            FileOutputStream out = new FileOutputStream("example.docx");
            doc.write(out);
            out.close();
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
}

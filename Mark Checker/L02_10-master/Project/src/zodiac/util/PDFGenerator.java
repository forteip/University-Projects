package zodiac.util;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import zodiac.definition.coursework.Assignment;
import zodiac.definition.coursework.Question;

public class PDFGenerator {

  private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
      Font.BOLD);
  private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
      Font.BOLD);
  Assignment assign;

  public PDFGenerator(Assignment assign) {
    this.assign = assign;
  }

  private static void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }

  /**
   * Generate PDF using current content.
   *
   * @return absolute path to that file upon success, else return null
   */
  public String generate() {
    try {
      Document document = new Document();
      File file = new File("./" + assign.getName() + ".pdf");
      FileOutputStream FILE = new FileOutputStream(file);
      PdfWriter.getInstance(document, FILE);
      document.open();
      addMetaData(document);
      addContent(document);
      document.close();
      return file.getAbsolutePath();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void addMetaData(Document document) {
    document.addTitle("Assignment " + assign.getName());
  }

  private void addContent(Document document) throws DocumentException {
    List<Question> ques = assign.getQuestionList();
    // get chapter

    Anchor anchor = new Anchor("Questions", catFont);
    Chapter catPart = new Chapter(new Paragraph(anchor), 1);

    for (int i = 0; i < ques.size(); i++) {
      Question q = ques.get(i);
      // get question
      String q_string = q.getQuestion();
      Paragraph subPara = new Paragraph(q_string, subFont);
      Section subCatPart = catPart.addSection(subPara);
      // adding answer
      List<String> ans = q.getAnswerList();
      for (int j = 0; j < ans.size(); j++) {
        Paragraph p = new Paragraph("     " + ans.get(j));
        subCatPart.add(p);
        if (j == ans.size() - 1) {
          addEmptyLine(p, 1);
        }
      }
    }
    // add to the document
    document.add(catPart);
  }
}

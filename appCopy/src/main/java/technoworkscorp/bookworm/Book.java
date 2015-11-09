package technoworkscorp.bookworm;

/**
 * Created by nitinbudhwar on 17/08/15.
 */
public class Book {
    private long id;
    private String book_name;
    private String book_cover;
    private String course_id;
    private String course_name;

    public String getBook_name() {
        return book_name;
    }
    public long getid(){ return id;}
    public void setid(long in_id){
        this.id = in_id;
    }
    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getThumbnail() {
        return book_cover;
    }

    public void setThumbnail(String book_cover) {
        this.book_cover = book_cover;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_name() {
        return course_name;
    }
}

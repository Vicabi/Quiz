import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    protected String category;
    protected Questions q1;
    protected Questions q2;
    protected Questions q3;

    protected List<Questions> questions;

    public Category(String category, Questions q1, Questions q2, Questions q3){
        this.category = category;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        questions = List.of(q1,q2,q3);
    }
    public String getCategory(){
        return category;
    }
}

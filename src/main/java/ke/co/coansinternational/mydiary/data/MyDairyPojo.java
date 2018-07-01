package ke.co.coansinternational.mydiary.data;

public class MyDairyPojo {
    String dairydate, diarytitle, diarynote;

    public MyDairyPojo() {
    }

    public MyDairyPojo(String dairydate, String diarytitle, String diarynote) {
        this.dairydate = dairydate;
        this.diarytitle = diarytitle;
        this.diarynote = diarynote;
    }

    public String getDairydate() {
        return dairydate;
    }

    public void setDairydate(String dairydate) {
        this.dairydate = dairydate;
    }

    public String getDiarytitle() {
        return diarytitle;
    }

    public void setDiarytitle(String diarytitle) {
        this.diarytitle = diarytitle;
    }

    public String getDiarynote() {
        return diarynote;
    }

    public void setDiarynote(String diarynote) {
        this.diarynote = diarynote;
    }
}

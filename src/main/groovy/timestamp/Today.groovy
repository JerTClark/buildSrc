package timestamp

import java.text.SimpleDateFormat

class Today {

    private String today

    public Today() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEMMMddyyyy-kkmm")
        Calendar calendar = Calendar.instance
        this.today = simpleDateFormat.format(calendar.getTime())
    }

    @Override
    public String toString() {
        return this.today;
    }

    public static void main(String[] args) {
        println new Today()
    }
}

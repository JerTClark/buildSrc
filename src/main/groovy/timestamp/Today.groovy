package timestamp

import java.text.SimpleDateFormat

class Today {

    private String today
    private String noClock

    public Today() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEMMMddyyyy-kkmm")
        SimpleDateFormat simpleDateFormatNoClock = new SimpleDateFormat("EEEMMMddyyyy")
        Calendar calendar = Calendar.instance
        Date date = calendar.getTime()
        this.today = simpleDateFormat.format(date)
        this.noClock = simpleDateFormatNoClock.format(date)
    }

    @Override
    public String toString() {
        this.today;
    }

    public String noClock() {
        this.noClock
    }

    public static void main(String[] args) {
        println new Today()
        println new Today().noClock
    }
}

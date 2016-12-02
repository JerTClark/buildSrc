package timestamp

import java.text.SimpleDateFormat

class Today {

    private String today
    private String noClock

    public Today() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEMMMddyyyy-kkmm")
        final SimpleDateFormat simpleDateFormatNoClock = new SimpleDateFormat("EEEMMMddyyyy")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
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

    public static void main(final String[] args) {
        println new Today()
        println new Today().noClock
    }
}

package org.snapper.buildsrc.timestamp

import java.text.SimpleDateFormat

/**
 * A class for obtaining a formatted date inside of a buildscript.
 */
class Today {

    /**A timestamp with a date and time segment.*/
    private String today

    /**A timestamp without an hour and minute segment.*/
    private String noClock

    Today() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEMMMddyyyy-kkmm")
        final SimpleDateFormat simpleDateFormatNoClock = new SimpleDateFormat("EEEMMMddyyyy")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
        this.today = simpleDateFormat.format(date)
        this.noClock = simpleDateFormatNoClock.format(date)
    }

    @Override
    String toString() {
        this.today;
    }

    String noClock() {
        this.noClock
    }

    static void main(final String[] args) {
        println new Today()
        println new Today().noClock
    }
}

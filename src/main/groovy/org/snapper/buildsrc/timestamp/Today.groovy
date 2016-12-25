package org.snapper.buildsrc.timestamp

import org.gradle.internal.impldep.bsh.This

import java.text.SimpleDateFormat

/**
 * A class for obtaining a formatted date inside of a buildscript.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Today {

    Today() { /*Empty constructor*/ }

    /**Obtain the default timestamp, {@link #getNumeric}.*/
    @Override
    String toString() {
        getNumeric()
    }

    /**
     * A timestamp with a date and time segment (i.e., "EEEMMMddyyyy-kkmm").
     * This method calls through to the default ({@link #toString}) implementation.
     */
    String getToday() {
        this.toString()
    }

    /**
     * A timestamp with a date and time segment (i.e., "MMM dd, yyyy (EEE.) 'at' h:mm a").
     * This translates to something like "Dec 24, 2016 (Sat.) at 6:55 pm."
     */
    static String getHumanReadable() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy (EEE.) 'at' h:mm a")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
        simpleDateFormat.format(date)
    }

    /**
     * A timestamp without an hour and minute segment (i.e., "MMM dd, yyyy").
     * This translates to something like "Dec 24, 2016."
     */
    static String getHumanReadableStandard() {
        final SimpleDateFormat simpleDateFormatNoClock = new SimpleDateFormat("MMM dd, yyyy")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
        simpleDateFormatNoClock.format(date)
    }

    /**
     * A timestamp without an hour and minute segment (i.e., "yyyyD").
     * This translates to the year and day of the year.
     */
    static String getNumericNoClock() {
        final SimpleDateFormat simpleDateFormatNoClock = new SimpleDateFormat("yyyyD")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
        simpleDateFormatNoClock.format(date)
    }

    /**
     * A timestamp represented as a strictly numeric date representation (i.e., "yyyyDHmsS").
     * This translates to the year, the day of the year, hour of day (0-23), minute of hour,
     * second in minute, and millisecond of the second.
     */
    static String getNumeric() {
        final SimpleDateFormat simpleDateFormatNumeric = new SimpleDateFormat("yyyyDHmsS")
        final Calendar calendar = Calendar.instance
        final Date date = calendar.getTime()
        simpleDateFormatNumeric.format(date)
    }

    static void main(final String[] args) {
        println "Default: " + new Today()
        println "No clock (human readable): " + humanReadableStandard
        println "No clock (numeric): " + numericNoClock
        println "Human readable: " + humanReadable
        println "Numeric: " + numeric
    }

}

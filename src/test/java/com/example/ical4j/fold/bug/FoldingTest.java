package com.example.ical4j.fold.bug;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;

class FoldingTest {

    @Test
    void testValidFolding() throws Exception {

        var expected = """
                BEGIN:VCALENDAR
                BEGIN:VEVENT
                DTSTART:20250317T100000Z
                DTEND:20250317T110000Z
                SUMMARY:Lorem ipsum dolor sit amet\\, consectetur adipiscing elit. Vestibu
                 lum in elit leo. Etiam quis venenatis lectus. Cras erat mauris\\, rhoncus
                  id felis eu\\, mollis malesuada lectus. Pellentesque at porta urna.
                DESCRIPTION:Lorem ipsum dolor sit amet\\, consectetur adipiscing elit. Ves
                 tibulum in elit leo. Etiam quis venenatis lectus. Cras erat mauris\\, rho
                 ncus id felis eu\\, mollis malesuada lectus. Pellentesque at porta urna.\s
                 Duis vitae purus odio. Etiam maximus malesuada sapien\\, vel consectetur\s
                 magna eleifend ut. Pellentesque consequat diam est\\, eu euismod tortor p
                 osuere non.
                END:VEVENT
                END:VCALENDAR
                """;

        var event = new VEvent();

        event.add(new DtStart<>(Instant.parse("2025-03-17T10:00:00Z")));
        event.add(new DtEnd<>(Instant.parse("2025-03-17T11:00:00Z")));
        event.add(new Summary("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum in elit leo. "
                + "Etiam quis venenatis lectus. Cras erat mauris, rhoncus id felis eu, mollis malesuada lectus. "
                + "Pellentesque at porta urna."));
        event.add(new Description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum in elit leo. "
                + "Etiam quis venenatis lectus. Cras erat mauris, rhoncus id felis eu, mollis malesuada lectus. "
                + "Pellentesque at porta urna. Duis vitae purus odio. Etiam maximus malesuada sapien, "
                + "vel consectetur magna eleifend ut. Pellentesque consequat diam est, eu euismod tortor posuere non."));

        event.remove(event.getDateTimeStamp());

        Calendar calendar = new Calendar();
        calendar.add(event);

        String result;
        try (var output = new StringWriter()) {
            var calendarOutputter = new CalendarOutputter();
            calendarOutputter.output(calendar, output);
            result = output.toString();
        }

        assertThat(result).isEqualToNormalizingNewlines(expected);

    }

    @Test
    void testMultibyteFolding() throws Exception {

        var expected = """
                BEGIN:VCALENDAR
                BEGIN:VEVENT
                DTSTART:20250317T100000Z
                DTEND:20250317T110000Z
                SUMMARY:🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂
                 🙂🙂🙂🙂🙂
                DESCRIPTION:🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂
                 🙂🙂🙂🙂🙂🙂
                END:VEVENT
                END:VCALENDAR
                """;

        var event = new VEvent();

        event.add(new DtStart<>(Instant.parse("2025-03-17T10:00:00Z")));
        event.add(new DtEnd<>(Instant.parse("2025-03-17T11:00:00Z")));
        event.add(new Summary("🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂"));
        event.add(new Description("🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂"));

        event.remove(event.getDateTimeStamp());

        Calendar calendar = new Calendar();
        calendar.add(event);

        String result;
        try (var output = new StringWriter()) {
            var calendarOutputter = new CalendarOutputter();
            calendarOutputter.output(calendar, output);
            result = output.toString();
        }

        assertThat(result).isEqualToNormalizingNewlines(expected);

    }

}

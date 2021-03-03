package com.kaarelkaasla.backend.helpers;

import com.kaarelkaasla.backend.entities.VisitDetails;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper class for parsing and processing a CSV file
 */
public class VisitDetailsHelper {

    public static List<VisitDetails> visitDetails(InputStream is) {
        BOMInputStream bomIn = new BOMInputStream(is); //in case a file has a byte order mark
        List<VisitDetails> visitDetailsList = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        try (CSVReader csvReader = new CSVReaderBuilder(
                new InputStreamReader(bomIn, StandardCharsets.UTF_8))
                .withCSVParser(csvParser)
                .withSkipLines(0)
                .build()) {
            List<String[]> entries = csvReader.readAll();
            final Map<String, Integer> mappedHeaders = mapHeaders(entries.get(0));
            List<String[]> data = entries.subList(1, entries.size());
            for (String[] entry : data) {
                String id = transformIdCode(entry[mappedHeaders.get("identification")]);
                if (!validateIdCodeDateOfBirth(id)) {
                    continue;
                }
                VisitDetails details = new VisitDetails(
                        entry[mappedHeaders.get("code")],
                        entry[mappedHeaders.get("dep")],
                        entry[mappedHeaders.get("visit_time")],
                        entry[mappedHeaders.get("first_name")],
                        entry[mappedHeaders.get("last_name")],
                        entry[mappedHeaders.get("email")],
                        id,
                        getDateOfBirth(id).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        getGender(id),
                        getAgeDuringVisit(id, entry[mappedHeaders.get("visit_time")])
                );
                visitDetailsList.add(details);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse the CSV file: " +
                    e.getMessage());
        } catch (CsvException e) {
            throw new RuntimeException("Failed to read the CSV file entries: " +
                    e.getMessage());
        }
        return visitDetailsList;
    }

    private static Map<String, Integer> mapHeaders(String[] headers) { //maps the order of a CSV file columns
        Map<String, Integer> mappedHeaders = new ConcurrentHashMap<>();
        for (int i = 0; i < headers.length; i++) {
            switch (headers[i]) {
                case "first":
                case "first_name":
                    mappedHeaders.put("first_name", i);
                    break;
                case "last":
                case "last_name":
                    mappedHeaders.put("last_name", i);
                    break;
                case "id_code":
                case "isikukood":
                    mappedHeaders.put("identification", i);
                    break;
                default:
                    mappedHeaders.put(headers[i], i);
                    break;
            }
        }
        return mappedHeaders;
    }

    private static String transformIdCode(String id) {
        return new BigDecimal(id.replace(',', '.')).toPlainString();
    }

    private static LocalDate getDateOfBirth(String id) { //returns the year of birth from ID-code
        int year = getBirthCentury(id) + Integer.parseInt(id.substring(1, 3));
        int month = Integer.parseInt(id.substring(3, 5));
        int day = Integer.parseInt(id.substring(5, 7));
        return LocalDate.of(year, month, day);
    }

    private static int getBirthCentury(String id) { //returns the birth century from ID-code
        int century;
        switch (id.charAt(0)) {
            case '1':
            case '2':
                century = 1800;
                break;
            case '3':
            case '4':
                century = 1900;
                break;
            case '5':
            case '6':
                century = 2000;
                break;
            default:
                century = 2100;
                break;
        }
        return century;
    }

    private static String getGender(String id) { //returns the gender from ID-code
        return (id.charAt(0) % 2 == 0) ? "Female" : "Male";
    }

    private static Integer getAgeDuringVisit(String id, String visit_time) { //returns the age during the visit
        LocalDate visitTime = LocalDate.parse(visit_time.substring(0, 10));
        LocalDate dateOfBirth = getDateOfBirth(id);
        Period period = Period.between(dateOfBirth, visitTime);
        return period.getYears();
    }

    private static boolean validateIdCodeDateOfBirth(String id) { //validates whether an ID-code is valid
        String year = String.valueOf(getBirthCentury(id) + Integer.parseInt(id.substring(1, 3)));
        String month = id.substring(3, 5);
        String day = id.substring(5, 7);

        try {
            LocalDate.parse(String.format("%s%s%s", year, month, day), DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}

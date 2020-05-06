package ua.nure.cherkashyn.hotel.db.dao;

import ua.nure.cherkashyn.hotel.db.entity.Apartment;
import ua.nure.cherkashyn.hotel.exception.DBException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;


/**
 * Interface ApartmentDAO.
 *
 * @author Vladimir Cherkashyn
 */
public interface ApartmentDAO {

    BigDecimal getMaxApartmentPrice() throws DBException;

    BigDecimal getMinApartmentPrice() throws DBException;

    List<Apartment> getBookedApartments(int offset,
                                        int howMany,
                                        long classId,
                                        int quantityOfRooms,
                                        BigDecimal priceFrom,
                                        BigDecimal priceUntil,
                                        boolean orderBy,
                                        LocalDate arrivalDate,
                                        LocalDate departureDate,
                                        Locale locale) throws DBException;

    int getQuantityOfApartmentsWithBookedStatus(long classId,
                                                int quantityOfRooms,
                                                BigDecimal priceFrom,
                                                BigDecimal priceUntil,
                                                LocalDate arrivalDate,
                                                LocalDate departureDate) throws DBException;

    List<Apartment> getOccupiedApartments(int offset,
                                          int howMany,
                                          long classId,
                                          int quantityOfRooms,
                                          BigDecimal priceFrom,
                                          BigDecimal priceUntil,
                                          boolean orderBy,
                                          LocalDate arrivalDate,
                                          LocalDate departureDate,
                                          Locale locale) throws DBException;

    int getQuantityOfApartmentsWithOccupiedStatus(long classId,
                                                  int quantityOfRooms,
                                                  BigDecimal priceFrom,
                                                  BigDecimal priceUntil,
                                                  LocalDate arrivalDate,
                                                  LocalDate departureDate) throws DBException;

    List<Apartment> getFreeApartments(int offset,
                                      int howMany,
                                      long classId,
                                      int quantityOfRooms,
                                      BigDecimal priceFrom,
                                      BigDecimal priceUntil,
                                      boolean orderBy,
                                      LocalDate arrivalDate,
                                      LocalDate departureDate,
                                      Locale locale) throws DBException;

    int getQuantityOfApartmentsWithFreeStatus(long classId,
                                              int quantityOfRooms,
                                              BigDecimal priceFrom,
                                              BigDecimal priceUntil,
                                              LocalDate arrivalDate,
                                              LocalDate departureDate) throws DBException;


}

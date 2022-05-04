/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.cipphonenumbervalidation.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GooglePhoneNumberLibraryServiceTest {

  private PhoneNumberLibraryService classUnderTest;

  @Before
  public void setUp() {
    classUnderTest = new PhoneNumberLibraryService();
  }

  @Test
  public void isValidPhoneNumber_isValidUkLandlineNumber_returnsTrue() {
    String validPhoneNumber = "01292123456";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
  public void isValidPhoneNumber_isValidUkMobileNumber_returnsTrue() {
    String validPhoneNumber = "07812345678";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
<<<<<<< HEAD
=======
  public void isValidPhoneNumber_isValidUkLandlineNumberWithPlus44_returnsTrue() {
    String validPhoneNumber = "+441292123456";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
  public void isValidPhoneNumber_isValidUkMobileNumberWithPlus44_returnsTrue() {
    String validPhoneNumber = "+447812345678";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
  public void isValidPhoneNumber_isValidROILandlineNumber_returnsTrue() {
    String validPhoneNumber = "+35312382300";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
<<<<<<< HEAD
=======
  public void isValidPhoneNumber_isValidVOIPNumber_returnsTrue() {
    // real life scam phone number that is in fact a VOIP number
    // its not really in Alloa - carrier network is "Net-Work Internet Ltd"
    String validPhoneNumber = "01259 333036";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
  public void isValidPhoneNumber_isValidFreephoneNumber_returnsTrue() {
    // BT customer services
    String validPhoneNumber = "0800800150";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
  public void isValidPhoneNumber_isValidPremiumNumber_returnsTrue() {
    // ITV competition
    String validPhoneNumber = "09068781119";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
  public void isValidPhoneNumber_isInValidNumber_returnsFalse() {
    String invalidPhoneNumber = "35312382300";
    boolean actual = classUnderTest.isValidPhoneNumber(invalidPhoneNumber);
    assertFalse(actual);
  }

<<<<<<< HEAD
=======
  @Test
  public void isValidPhoneNumber_isBlank_returnsFalse() {
    String invalidPhoneNumber = "";
    boolean actual = classUnderTest.isValidPhoneNumber(invalidPhoneNumber);
    assertFalse(actual);
  }

  @Test
  public void isValidPhoneNumber_isEmptySpace_returnsFalse() {
    String invalidPhoneNumber = " ";
    boolean actual = classUnderTest.isValidPhoneNumber(invalidPhoneNumber);
    assertFalse(actual);
  }

  @Test
  public void isValidPhoneNumber_isNotAllowed_returnsFalse() {
    String invalidPhoneNumber = "999";
    boolean actual = classUnderTest.isValidPhoneNumber(invalidPhoneNumber);
    assertFalse(actual);
  }

>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
}

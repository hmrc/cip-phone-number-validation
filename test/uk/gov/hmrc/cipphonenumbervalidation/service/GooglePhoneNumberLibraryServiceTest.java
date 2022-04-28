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

  private GooglePhoneNumberLibraryService classUnderTest;

  @Before
  public void setUp() {
    classUnderTest = new GooglePhoneNumberLibraryService();
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
  public void isValidPhoneNumber_isValidROILandlineNumber_returnsTrue() {
    String validPhoneNumber = "+35312382300";
    boolean actual = classUnderTest.isValidPhoneNumber(validPhoneNumber);
    assertTrue(actual);
  }

  @Test
  public void isValidPhoneNumber_isInValidNumber_returnsFalse() {
    String invalidPhoneNumber = "35312382300";
    boolean actual = classUnderTest.isValidPhoneNumber(invalidPhoneNumber);
    assertFalse(actual);
  }

}

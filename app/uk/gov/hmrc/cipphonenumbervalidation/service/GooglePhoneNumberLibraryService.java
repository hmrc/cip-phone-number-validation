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

///*
// * Copyright 2022 HM Revenue & Customs
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package uk.gov.hmrc.cipphonenumbervalidation.service;
//
//import com.google.i18n.phonenumbers.NumberParseException;
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
//
//public class GooglePhoneNumberLibraryService {
//
//  private PhoneNumberUtil phoneNumberUtil;
//
//  public GooglePhoneNumberLibraryService() {
//    this.phoneNumberUtil = PhoneNumberUtil.getInstance();
//  }
//
//  public boolean isValidPhoneNumber(String phoneNumber) {
//    try {
//      PhoneNumber phoneNumberGoogle = null;
//
//      if(isUkPhoneNumber(phoneNumber)) {
//        String ukPhoneNumber = createUKNumber(phoneNumber);
//        phoneNumberGoogle = phoneNumberUtil.parse(ukPhoneNumber, "UK");
//      } else {
//        // the phone number must begin with '+'
//        PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, "");
//        int countryCode = numberProto.getCountryCode();
//        phoneNumberGoogle = phoneNumberUtil.parse(phoneNumber, Integer.toString(countryCode));
//      }
//
//      boolean isValid = phoneNumberUtil.isValidNumber(phoneNumberGoogle);
//      if(isValid) {
//        return true;
//      } else {
//        return false;
//      }
//    } catch (NumberParseException e) {
//<<<<<<< HEAD
//      System.err.println("NumberParseException was thrown: " + e);
//=======
//      System.err.println("Google Library: NumberParseException was thrown: " + e);
//      return false;
//    } catch (Exception e){
//      System.err.println("Google Library: Exception was thrown: " + e);
//>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
//      return false;
//    }
//  }
//
//  private String createUKNumber(String input) {
//    String firstZeroRemoved = input.substring(1);
//    return "+44" + firstZeroRemoved;
//  }
//
//  private boolean isUkPhoneNumber(String input) {
//<<<<<<< HEAD
//=======
//    if (input == null || input.isEmpty()) {
//      return false;
//    }
//>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
//    char firstChar = input.charAt(0);
//    if('0' == firstChar) {
//      return true;
//    } else {
//      return false;
//    }
//  }
//
//}

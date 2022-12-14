/* Generated By:JJTree&JavaCC: Do not edit this line. FilterParserConstants.java */
/**
 * [Phillip Watson] ("COMPANY") CONFIDENTIAL Unpublished Copyright © 2019-2020 Phillip Watson,
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of COMPANY. The
 * intellectual and technical concepts contained herein are proprietary to COMPANY and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from COMPANY. Access to the source code
 * contained herein is hereby forbidden to anyone except current COMPANY employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure of
 * this source code, which includes information that is confidential and/or proprietary, and is a
 * trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR
 * PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF
 * COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES.
 * THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY
 * ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL
 * ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.hillayes.query.filter.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface FilterParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int AND = 6;
  /** RegularExpression Id. */
  int OR = 7;
  /** RegularExpression Id. */
  int NOT = 8;
  /** RegularExpression Id. */
  int EQ = 9;
  /** RegularExpression Id. */
  int NE = 10;
  /** RegularExpression Id. */
  int GT = 11;
  /** RegularExpression Id. */
  int LT = 12;
  /** RegularExpression Id. */
  int GE = 13;
  /** RegularExpression Id. */
  int LE = 14;
  /** RegularExpression Id. */
  int LPAREN = 15;
  /** RegularExpression Id. */
  int RPAREN = 16;
  /** RegularExpression Id. */
  int COMMA = 17;
  /** RegularExpression Id. */
  int TOUPPER = 18;
  /** RegularExpression Id. */
  int TOLOWER = 19;
  /** RegularExpression Id. */
  int CONTAINS = 20;
  /** RegularExpression Id. */
  int ENDSWITH = 21;
  /** RegularExpression Id. */
  int STARTSWITH = 22;
  /** RegularExpression Id. */
  int NOTNULL = 23;
  /** RegularExpression Id. */
  int ISNULL = 24;
  /** RegularExpression Id. */
  int DIGIT = 25;
  /** RegularExpression Id. */
  int ALPHA = 26;
  /** RegularExpression Id. */
  int INTEGER = 27;
  /** RegularExpression Id. */
  int DOT = 28;
  /** RegularExpression Id. */
  int NUMERIC = 29;
  /** RegularExpression Id. */
  int QUOTED_TEXT = 30;
  /** RegularExpression Id. */
  int IDENTIFIER = 31;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\n\"",
    "\"\\f\"",
    "\"and\"",
    "\"or\"",
    "\"not\"",
    "\"eq\"",
    "\"ne\"",
    "\"gt\"",
    "\"lt\"",
    "\"ge\"",
    "\"le\"",
    "\"(\"",
    "\")\"",
    "\",\"",
    "\"upper\"",
    "\"lower\"",
    "\"contains\"",
    "\"endswith\"",
    "\"startswith\"",
    "\"notnull\"",
    "\"isnull\"",
    "<DIGIT>",
    "<ALPHA>",
    "<INTEGER>",
    "\".\"",
    "<NUMERIC>",
    "<QUOTED_TEXT>",
    "<IDENTIFIER>",
  };

}

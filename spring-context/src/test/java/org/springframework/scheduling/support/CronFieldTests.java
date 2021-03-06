/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.scheduling.support;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.springframework.scheduling.support.BitSetAssert.assertThat;

/**
 * @author Arjen Poutsma
 */
public class CronFieldTests {

	@Test
	void parse() {
		assertThat(CronField.parseSeconds("42").bits()).hasUnsetRange(0, 41).hasSet(42).hasUnsetRange(43, 59);
		assertThat(CronField.parseMinutes("1,2,5,9").bits()).hasUnset(0).hasSet(1, 2).hasUnset(3,4).hasSet(5).hasUnsetRange(6,8).hasSet(9).hasUnsetRange(10,59);
		assertThat(CronField.parseSeconds("0-4,8-12").bits()).hasSetRange(0, 4).hasUnsetRange(5,7).hasSetRange(8, 12).hasUnsetRange(13,59);
		assertThat(CronField.parseHours("0-23/2").bits()).hasSet(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22).hasUnset(1,3,5,7,9,11,13,15,17,19,21,23);
		assertThat(CronField.parseDaysOfWeek("0").bits()).hasUnsetRange(0, 6).hasSet(7, 7);
		assertThat(CronField.parseSeconds("57/2").bits()).hasUnsetRange(0, 56).hasSet(57).hasUnset(58).hasSet(59);
	}

	@Test
	void invalidRange() {
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseSeconds(""));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseSeconds("0-12/0"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseSeconds("60"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseMinutes("60"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseDaysOfMonth("0"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseDaysOfMonth("32"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseMonth("0"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseMonth("13"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseDaysOfWeek("8"));
		assertThatIllegalArgumentException().isThrownBy(() -> CronField.parseSeconds("20-10"));
	}

	@Test
	void parseWildcards() {
		assertThat(CronField.parseSeconds("*").bits()).hasSetRange(0, 60);
		assertThat(CronField.parseMinutes("*").bits()).hasSetRange(0, 60);
		assertThat(CronField.parseHours("*").bits()).hasSetRange(0, 23);
		assertThat(CronField.parseDaysOfMonth("*").bits()).hasUnset(0).hasSetRange(1, 31);
		assertThat(CronField.parseDaysOfMonth("?").bits()).hasUnset(0).hasSetRange(1, 31);
		assertThat(CronField.parseMonth("*").bits()).hasUnset(0).hasSetRange(1, 12);
		assertThat(CronField.parseDaysOfWeek("*").bits()).hasUnset(0).hasSetRange(1, 7);
		assertThat(CronField.parseDaysOfWeek("?").bits()).hasUnset(0).hasSetRange(1, 7);
	}

	@Test
	void names() {
		assertThat(CronField.parseMonth("JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC").bits())
				.hasUnset(0).hasSetRange(1, 12);
		assertThat(CronField.parseDaysOfWeek("SUN,MON,TUE,WED,THU,FRI,SAT").bits())
				.hasUnset(0).hasSetRange(1, 7);
	}

}

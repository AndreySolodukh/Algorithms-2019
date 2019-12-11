package lesson6

import kotlin.test.assertEquals

abstract class AbstractDynamicTests {
    fun longestCommonSubSequence(longestCommonSubSequence: (String, String) -> String) {
        assertEquals("", longestCommonSubSequence("а здесь", "нЕт_СовпАДЕний"))
        assertEquals("MY", longestCommonSubSequence("MARRY ME", "BELLAMY"))
        assertEquals("о а ка какао", longestCommonSubSequence("о, а как какао?", "као као ооо као какао ооо"))
        assertEquals("ONO GA", longestCommonSubSequence("BRAKA MONO GA", "DOITSU NO KAGAKU WA SEKAI ICHI"))
        assertEquals(
            " AINDOE IIIIIIIIIII",
            longestCommonSubSequence(
                "SNAKE HEAD EATING ITS HEAD ON THE OPPOSITE SIIIIIIIIIIIIDE",
                "I PALINDROME IIIIIIIIIII"
            )
        )
        assertEquals(
            "hee e kebard a",
            longestCommonSubSequence(
                "here comes keyboard rape",
                "jalwdkjalkdwalkdlakfhlakfwalwkd nawke fakj befawejf akwjebf aw kjefaw" +
                        "lkenfALlknfclaknflakbflakflike fnwfviaerghawklgb awjwldjalfwhalah lwahwdlahwla"
            )
        )

        assertEquals("", longestCommonSubSequence("мой мир", "я"))
        assertEquals("1", longestCommonSubSequence("1", "1"))
        assertEquals("13", longestCommonSubSequence("123", "13"))
        assertEquals("здс", longestCommonSubSequence("здравствуй мир", "мы здесь"))
        assertEquals("emt ole", longestCommonSubSequence("nematode knowledge", "empty bottle"))
        val expectedLength = "e kerwelkkd r".length
        assertEquals(
            expectedLength, longestCommonSubSequence(
                "oiweijgw kejrhwejelkrw kjhdkfjs hrk",
                "perhkhk lerkerorwetp lkjklvvd durltr"
            ).length, "Answer must have length of $expectedLength, e.g. 'e kerwelkkd r' or 'erhlkrw kjk r'"
        )
        val expectedLength2 = """ дд саы чтых,
евшнео ваа се сви дн.
        """.trimIndent().length
        assertEquals(
            expectedLength2, longestCommonSubSequence(
                """
Мой дядя самых честных правил,
Когда не в шутку занемог,
Он уважать себя заставил
И лучше выдумать не мог.
                """.trimIndent(),
                """
Так думал молодой повеса,
Летя в пыли на почтовых,
Всевышней волею Зевеса
Наследник всех своих родных.
                """.trimIndent()
            ).length, "Answer must have length of $expectedLength2"
        )
    }

    fun longestIncreasingSubSequence(longestIncreasingSubSequence: (List<Int>) -> List<Int>) {
        assertEquals(listOf(999), longestIncreasingSubSequence(listOf(999)))
        assertEquals(listOf(1, 2, 3, 5, 8, 13, 21), longestIncreasingSubSequence(listOf(1, 1, 2, 3, 5, 8, 13, 21)))
        assertEquals(listOf(3, 4, 5, 6, 9), longestIncreasingSubSequence(listOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 9)))
        assertEquals(
            listOf(24, 33, 56, 94, 138, 223, 246, 306, 412, 544, 584, 665, 679, 766, 787, 821, 846, 887, 902, 937),
            longestIncreasingSubSequence(
                // (200 чисел)
                listOf(
                    823, 290, 528, 984, 653, 350, 410, 647, 649, 167, 747, 447, 343, 809, 205, 297, 536, 994, 992,
                    508, 241, 24, 731, 195, 722, 711, 949, 367, 289, 820, 33, 514, 321, 960, 179, 760, 21, 880, 950,
                    499, 957, 56, 210, 399, 318, 860, 532, 28, 996, 94, 320, 974, 32, 314, 705, 776, 138, 316, 596,
                    626, 513, 11, 268, 223, 892, 752, 686, 598, 201, 882, 246, 54, 216, 556, 129, 553, 782, 120,
                    454, 87, 178, 124, 306, 35, 412, 136, 633, 954, 928, 621, 709, 84, 544, 839, 707, 584, 227, 261,
                    338, 43, 154, 225, 80, 236, 92, 665, 431, 131, 679, 766, 156, 275, 256, 982, 856, 118, 881, 121,
                    202, 749, 277, 986, 558, 475, 932, 196, 914, 901, 787, 878, 322, 821, 250, 667, 671, 160, 183,
                    224, 846, 157, 840, 732, 243, 548, 511, 276, 988, 428, 698, 200, 413, 66, 811, 444, 887, 745, 902,
                    262, 689, 88, 651, 381, 557, 863, 230, 865, 299, 849, 346, 937, 922, 430, 325, 660
                )
            )
        )

        assertEquals(listOf(), longestIncreasingSubSequence(listOf()))
        assertEquals(listOf(1), longestIncreasingSubSequence(listOf(1)))
        assertEquals(listOf(1, 2), longestIncreasingSubSequence(listOf(1, 2)))
        assertEquals(listOf(2), longestIncreasingSubSequence(listOf(2, 1)))
        assertEquals(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            longestIncreasingSubSequence(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        )
        assertEquals(listOf(2, 8, 9, 12), longestIncreasingSubSequence(listOf(2, 8, 5, 9, 12, 6)))
        assertEquals(
            listOf(23, 34, 56, 87, 91, 98, 140, 349), longestIncreasingSubSequence(
                listOf(
                    23, 76, 34, 93, 123, 21, 56, 87, 91, 12, 45, 98, 140, 12, 5, 38, 349, 65, 94,
                    45, 76, 15, 99, 100, 88, 84, 35, 88
                )
            )
        )
    }

    fun shortestPathOnField(shortestPathOnField: (String) -> Int) {
        // (6x6)
        assertEquals(20, shortestPathOnField("input/field_in7.txt"))
        // (8x10)
        assertEquals(33, shortestPathOnField("input/field_in8.txt"))
        // (12x20)
        assertEquals(169, shortestPathOnField("input/field_in9.txt"))

        assertEquals(1, shortestPathOnField("input/field_in2.txt"))
        assertEquals(12, shortestPathOnField("input/field_in1.txt"))
        assertEquals(43, shortestPathOnField("input/field_in3.txt"))
        assertEquals(28, shortestPathOnField("input/field_in4.txt"))
        assertEquals(222, shortestPathOnField("input/field_in5.txt"))
        assertEquals(15, shortestPathOnField("input/field_in6.txt"))
    }

}
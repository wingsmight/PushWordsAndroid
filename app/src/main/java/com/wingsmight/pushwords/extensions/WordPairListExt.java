package com.wingsmight.pushwords.extensions;

import com.wingsmight.pushwords.data.WordPair;

import java.util.ArrayList;
import java.util.Collections;

public final class WordPairListExt {
    public static void sortByChangingDate(ArrayList<WordPair> wordPairs) {
        Collections.sort(wordPairs, (lhs, rhs) ->
                rhs.getChangingDate().compareTo(lhs.getChangingDate()));
    }
}

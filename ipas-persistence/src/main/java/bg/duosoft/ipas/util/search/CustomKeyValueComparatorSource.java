package bg.duosoft.ipas.util.search;

import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.util.BytesRef;

import java.util.Map;


public class CustomKeyValueComparatorSource extends FieldComparatorSource {
    Map<String, String> valuesMap;
    public CustomKeyValueComparatorSource(Map<String, String> valuesMap) {
        this.valuesMap = valuesMap;
    }

    @Override
    public FieldComparator<BytesRef> newComparator(final String fieldName, final int numHits, final int sortPos,
                                             boolean reversed) {
        return new FieldComparator.TermValComparator(numHits, fieldName, reversed){

            public int compareValues(BytesRef val1, BytesRef val2) {
                String k1 = new String(val1.bytes, val1.offset, val1.length);
                String k2 = new String(val2.bytes, val2.offset, val2.length);
                String v1 = valuesMap.get(k1);
                String v2 = valuesMap.get(k2);
                if (v1 == null) {
                    if (v2 == null) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (v2 == null) {
                    return -1;
                }
                return v1.toLowerCase().compareTo(v2.toLowerCase());

            }
        };
    }
}
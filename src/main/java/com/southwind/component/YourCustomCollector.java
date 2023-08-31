

package com.southwind.component;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/4/25 16:56
 */
public class YourCustomCollector extends Collector {

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        // With no labels.
        mfs.add(new GaugeMetricFamily("my_gauge", "help", 42));
        // With labels
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily("my_other_gauge", "help", Arrays.asList("labelname"));
        labeledGauge.addMetric(Arrays.asList("foo"), new Random().nextInt(5));
        labeledGauge.addMetric(Arrays.asList("bar"), new Random().nextInt(5));
        mfs.add(labeledGauge);
        return mfs;
    }
}

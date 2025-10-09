<template>
  <div ref="plotlyDiv" :style="style || {width: '100%', height: '420px'}"></div>
</template>
<script setup>
import { ref, watch, onMounted } from 'vue'
import Plotly from 'plotly.js-dist-min'
const props = defineProps({
  data: Array,
  layout: Object,
  style: Object
})
const plotlyDiv = ref(null)
function renderPlot() {
  if (plotlyDiv.value) {
    Plotly.newPlot(plotlyDiv.value, props.data, props.layout, {responsive: true})
  }
}
watch(() => [props.data, props.layout], renderPlot, {deep: true})
onMounted(renderPlot)
</script> 
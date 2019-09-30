<template>
    <div>modal msg
        <button @click="$emit('close')">Close</button>
    </div>
</template>

<script>
    export default {
        name: "modalMsg"
    }
</script>

<style scoped>

</style>
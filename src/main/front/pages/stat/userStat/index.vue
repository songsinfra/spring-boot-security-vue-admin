<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card table-responsive">
                    <b-table
                            :select-mode="'single'"
                            :items="items"
                            :fields="fields"
                            :bordered="true"
                    >
                        <template v-slot:thead-top="data">
                            <b-tr>
                                <b-th colspan="1"><span class="sr-only">Name and ID</span></b-th>
                                <b-th class="text-center">전전월</b-th>
                                <b-th class="text-center" colspan="2">전월</b-th>
                                <b-th class="text-center" colspan="3">금월</b-th>
                            </b-tr>
                        </template>
                    </b-table>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    export default {
        name: "index",

        beforeMount() {
            this.selectJoinUserStat();
        },

        data() {
            return {
                fields: [],
                items:[],
            };
        },

        computed:{
            date(){
                return this.$moment().format('YYYYMMDD');
            }
        },

        methods:{
            async selectJoinUserStat() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/stat/selectJoinUserStat', { date: this.date});
                    this.items = data.data;

                    this.initStatData(this.date);
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            initStatData(date) {
                const DATE_FORMAT = 'YYYY.MM';
                const currentDate = this.$moment(date, 'YYYYMMDD');
                const currentMonthData = `${currentDate.format(DATE_FORMAT)}.01 ~ ${currentDate.format('DD')}`;

                const prevMonthData = currentDate.clone().subtract(2, 'months').format(DATE_FORMAT);
                const lastMonthData = currentDate.clone().subtract(1, 'months').format(DATE_FORMAT);

                const lastYear = currentDate.clone().subtract(1, 'years');
                const lastYearData = `${lastYear.format(DATE_FORMAT)}.01 ~ ${lastYear.format('DD')}`;

                this.fields = [
                    {key: 'joinType', label: '기간'},
                    {key: 'prevMonthData', label: prevMonthData},
                    {key: 'lastMonthData', label: lastMonthData},
                    {key: 'lastMonthRate', label: '전월대비'},
                    {key: 'lastYearData', label: lastYearData},
                    {key: 'currentMonthData', label: currentMonthData},
                    {key: 'lastYearRate', label: '전년대비'},
                ];
            },
        },
    }
</script>

<style scoped>

</style>
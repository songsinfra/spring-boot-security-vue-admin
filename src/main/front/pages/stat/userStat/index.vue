<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0">
                        <ul class="list-inline float-right m-0">
                            <li>
                                검색날짜
                            </li>
                            <li>
                                <b-form-input
                                        v-model="today"
                                        :size="'sm'"
                                        type="date"
                                ></b-form-input>
                            </li>
                            <li>
                                <b-button @click="selectJoinUserStat" class="btn-primary">검색</b-button>
                            </li>
                            <li>
                                <download-excel
                                        :class="'btn btn-primary btn-secondary'"
                                        :name    = "'가입자통계.xls'"
                                        :fields="excelFields"
                                        :data='items'>
                                    엑셀
                                </download-excel>
                            </li>
                        </ul>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        @head-clicked="clickHeaderOfTable"
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
                                    <template v-slot:head(prevMonthData)="data">
                                        <span class="text-info">11{{ data.label.toUpperCase() }}</span>
                                    </template>
                                </b-table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <modal-stat-month-list
                :date="detailDate"
                :date-type="detailDateType"
        />
    </section>
</template>

<script>
    import ModalStatMonthList from '~/components/pages/stat/userStat/ModalStatMonthList.vue';
    import JsonExcel from 'vue-json-excel';

    export default {
        name: "index",

        components: {
            downloadExcel : JsonExcel,
            ModalStatMonthList
        },

        beforeMount() {
            this.selectJoinUserStat();
        },

        data() {
            return {
                fields: [],
                items:[],
                today: '',
                excelFields:{},
                detailDate:'',
                detailDateType:'',
            };
        },

        methods:{
            async selectJoinUserStat() {
                try {
                    if(!this.today) this.today =  this.$moment().subtract(1, 'days').format('YYYY-MM-DD');

                    const date = this.today.replace(/-/g, '');
                    const {data} = await this.$axios.post(process.env.contextPath + '/stat/selectJoinUserStat', { date});
                    this.items = data.data;

                    this.initStatData(date);
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

                this.excelFields = {
                    '기간': 'joinType',
                    [prevMonthData]: 'prevMonthData',
                    [lastMonthData]: 'lastMonthData',
                    '전월대비': 'lastMonthRate',
                    [lastYearData]: 'lastYearData',
                    [currentMonthData]: 'currentMonthData',
                    '전년대비': 'lastYearRate',
                };
            },

            popupDetail(date, dateType) {
                this.detailDate = date;
                this.detailDateType = dateType

                this.$nextTick(async () => {
                    this.$bvModal.show('modal_join_stat_detail');
                });
            },

            clickHeaderOfTable(arg, {key, label}) {
                debugger;
                if (key === 'prevMonthData' || key === 'lastMonthData') {
                    const date = label.replace(/\./g, '');
                    this.popupDetail(date, 'MONTH');
                } else if (key === 'lastYearData' || key === 'currentMonthData') {
                    const date = label.replace('01 ~ ', '').replace(/\./g, '');
                    this.popupDetail(date, 'DAY');
                }

                console.log(arg);
            }

        },
    }
</script>

<style scoped>

</style>
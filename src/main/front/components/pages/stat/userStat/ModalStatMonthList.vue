<template>
    <b-modal
            id="modal_join_stat_detail"
            centered
            ref="modal"
            @show="showModal"
            :title="'상세 가입자 리스트'">
        <div class="card">
            <div class="card-header pb-0">
                <ul class="list-inline float-right m-0">
                    <li>
                        <download-excel
                                :class="'btn btn-primary btn-secondary'"
                                :name    = "'상세가입자리스트.xls'"
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
                                :select-mode="'single'"
                                :items="items"
                                :fields="fields"
                                :bordered="true"
                        >
                        </b-table>
                    </div>
                </div>
            </div>
        </div>
    </b-modal>
</template>

<script>
    import JsonExcel from 'vue-json-excel';

    export default {
        props: ['date', 'dateType'],
        name: "ModalStatMonthList",
        components: {downloadExcel: JsonExcel},
        data() {
            return {
                items : [],
                fields : [
                    {key: 'signupDate', label:  '가입일'},
                    {key: 'freeUserCnt', label: '무료'},
                    {key: 'paidUserCnt', label: '유료'},
                    {key: 'totUserCnt', label:  '전체'},
                ],
                excelFields: {
                    '가입일' : 'signupDate',
                    '무료' : 'freeUserCnt',
                    '유료' : 'paidUserCnt',
                    '전체' : 'totUserCnt',
                },
            };
        },

        methods:{
            async showModal() {
                try {
                    this.$nextTick(async () => {
                        this.selectJoinStatDetailList();
                    });
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async selectJoinStatDetailList() {
                const {startDt, endDt} = this.$props.dateType === 'MONTH' ? this.getMonth() : this.getDay();

                const {data} = await this.$axios.$post(process.env.contextPath + '/stat/selectJoinStatDetailList', {startDt, endDt})
                this.items = data;
            },

            getMonth() {
                const currentDate = this.$moment(this.$props.date,'YYYYMM');
                const startDt = currentDate.startOf('month').format('YYYYMMDD');
                const endDt = currentDate.endOf('month').format("YYYYMMDD");

                return {startDt, endDt};
            },

            getDay() {
                const currentDate = this.$moment(this.$props.date,'YYYYMMDD');
                const startDt = currentDate.clone().startOf('month').format('YYYYMMDD');
                const endDt = currentDate.format("YYYYMMDD");

                return {startDt, endDt};
            }


        }
    }
</script>

<style scoped>

</style>
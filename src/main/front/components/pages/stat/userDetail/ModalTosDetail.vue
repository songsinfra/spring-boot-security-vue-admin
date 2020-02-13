<template>
    <b-modal
            id="modal_Tos_detail"
            centered
            :size="'lg'"
            ref="modal"
            @show="showModal"
            @ok.prevent="submit"
            :title="'약관 동의 상세 수정'">
        <div class="card">
            <div class="card-content collapse show">
                <div class="card-body">
                    <div class="table-responsive">
                        <b-table
                                :select-mode="'single'"
                                :items="items"
                                :fields="fields"
                        >
                            <template v-slot:isAgree="{ item }">
                                <b-checkbox v-model="item.isAgree" value="1" unchecked-value="0" :disabled="item.tosManYn === 'Y'" :checked="item.isAgree === '1'"></b-checkbox>
                            </template>
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
        props: ['gfnId'],
        name: "ModalStatMonthList",
        components: {downloadExcel: JsonExcel},
        data() {
            return {
                items : [],
                fields : [
                    {key: 'isAgree',    label: '선택'},
                    {key: 'tosNo',      label: '약관일련번호'},
                    {key: 'tosNm',      label: '약관명'},
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
                        this.selectGfnTosList();
                    });
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async selectGfnTosList() {
                try {
                    const params = {
                        gfnId: this.$props.gfnId
                    };
                    const {data} = await this.$axios.$post(process.env.contextPath + '/stat/selectGfnTosList', params);
                    this.items = data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async submit() {
                try {
                    const agreeList = this.items.filter(i => i.tosManYn === 'N' && i.isAgree === '1').map(i => i.tosNo).join(',');
                    const params = {
                        agreeList: agreeList,
                        gfnId: this.$props.gfnId,
                    };

                    const {data} = await this.$axios.$post(process.env.contextPath + '/stat/updateAcceptTosList', params);
                    await this.$bvModal.msgBoxOk(data.message);
                    this.$eventBus.$emit('refreshUserDetail');
                    this.$refs.modal.hide();
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>

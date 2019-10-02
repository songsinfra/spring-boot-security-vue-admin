<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header p-1">
                        <ul class="list-inline float-right m-0">
                            <li><b-button @click="deleteMbrInfo" class="btn-danger">삭제</b-button></li>
                            <li><b-button @click="createMbrInfo" class="btn-primary">등록</b-button></li>
                        </ul>
                    </div>
                    <div class="card-content collapse show">
                        <div class="card-body">
                            <div class="table-responsive">
                                <b-table
                                        :items="items"
                                        :fields="fields"
                                >
                                    <template v-slot:select="{item}">
                                        <b-form-checkbox
                                                v-model="item.checked"
                                                name="checkbox-1"
                                                value="Y"
                                                unchecked-value="N"
                                        >
                                        </b-form-checkbox>
                                    </template>
                                    <template v-slot:action="{item}">
                                        <b-button
                                                variant="danger"
                                                @click="updateMbrInfo(item)"
                                        >수정</b-button>
                                    </template>
                                </b-table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <modal-mbr-info
                :selected-mbr-info="selectedMbrInfo"
                :state="modalState"
        />
    </section>
</template>



<script>
    import axios from 'axios';
    import ModalMbrInfo from '~/components/pages/mbrInfoList/ModalMbrInfo.vue';


    export default {
        name: "index",
        layout: 'default',
        components: {ModalMbrInfo},

        data() {
            return {
                fields: [
                    {key: 'select', label: '선택'},
                    {key: '', label: '번호'},
                    {key: 'codeNm', label: '구분'},
                    {key: 'mbrId', label: 'ID'},
                    {key: 'mbrNm', label: '이름'},
                    // {key: 'mbrCompany', label: '회사'},
                    // {key: 'mbrDptmt', label: '부서'},
                    {key: 'tel', label: 'CTN'},
                    {key: 'email', label: 'Email'},
                    {key: 'loginEndDt', label: '마지막접속일'},
                    {key: 'useYn', label: '사용여부',
                        formatter:(value)=> value === 'Y' ? '사용' : '미사용'
                    },
                    {key: 'action', label: '수정'},
                ],
                items: [],
                selectedMbrInfo: {},
                modalState: ''
            }
        },

        beforeMount() {
            this.getMbrList();
        },

        async created() {
            this.$eventBus.$on('refreshMbrList', this.getMbrList);
        },

        methods:{
            async getMbrList() {
                try {
                    const {data} = await axios.post('/api/mbr/getMemberList', { searchUseYn: this.searchUseYn});
                    this.items = data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async createMbrInfo() {
                this.modalState = "CREATE";
                this.$bvModal.show('modal_mbrInfo');
            },

            async updateMbrInfo(item) {
                this.modalState = "UPDATE";
                this.selectedMbrInfo = item;
                this.$bvModal.show('modal_mbrInfo');
            },

            async deleteMbrInfo() {
                try {
                    const deleteMbrIdList = this.items.filter(item => item.checked === 'Y')
                                                      .map(item=>item.mbrId);

                    if(deleteMbrIdList.length === 0){
                        await this.$bvModal.msgBoxOk('선택된 회원이 없습니다');
                        return;
                    }

                    const isOk = await this.$bvModal.msgBoxConfirm("선택된 회원을 삭제 하시겠습니까?");
                    if (!isOk) return;

                    const {data} = await this.$axios.post('/api/mbr/deleteMbr',{
                        mbrIdList : deleteMbrIdList
                    });

                    await this.getMbrList();
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },
        }

    }
</script>

<style scoped>

</style>
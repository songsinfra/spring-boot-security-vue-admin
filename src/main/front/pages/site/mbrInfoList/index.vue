<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <section id="collapsible">
        <div class="row">
            <div class="col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header pb-0" v-if="isAdmin()">
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
                                        :per-page="perPage"
                                        :current-page="currentPage"
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
                                <b-pagination
                                        v-model="currentPage"
                                        :total-rows="tableRows"
                                        :per-page="perPage"
                                        aria-controls="my-table"
                                        :align="'center'"
                                ></b-pagination>
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
    import ModalMbrInfo from '~/components/pages/mbrInfoList/ModalMbrInfo.vue';


    export default {
        name: "index",
        layout: 'default',
        components: {ModalMbrInfo},

        beforeMount() {
            this.getMbrList();
        },

        async created() {
            this.$eventBus.$on('refreshMbrList', this.getMbrList);
        },

        computed: {
            tableRows() {
                return this.items.length
            },
            user() {
                return this.$store.state.login.authUser;
            }
        },

        data() {
            return {
                fields: [
                    {key: 'select', label: '선택'},
                    {key: '', label: '번호'},
                    {key: 'codeNm', label: '구분', tdClass:'pr-1'},
                    {key: 'mbrId', label: 'ID'},
                    {key: 'mbrNm', label: '이름',
                        formatter:(value => value.replace(/(?<=.{1}).+(?=.{1})/, "*"))
                    },
                    {key: 'tel', label: 'CTN',
                        formatter: (value => value.replace(/(?<=.{3}).+(?=.{4})/, "****"))
                    },
                    {key: 'email', label: 'Email',
                        formatter: (value => value.replace(/(?<=.{2}).+(?=@.+)/, "***"))
                    },
                    {key: 'loginEndDt', label: '마지막접속일'},
                    {key: 'useYn', label: '사용여부',
                        formatter:(value)=> value === 'Y' ? '사용' : '미사용'
                    },
                    {key: 'action', label: '수정'},
                ],
                items: [],
                selectedMbrInfo: {},
                modalState: '',
                perPage: 10,
                currentPage: 1,
            }
        },

        methods:{
            async getMbrList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/mbr/getMemberList', { searchUseYn: this.searchUseYn});
                    this.items = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async createMbrInfo() {
                this.modalState = "CREATE";
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_mbrInfo');
                });
            },

            async updateMbrInfo(item) {
                this.modalState = "UPDATE";
                this.selectedMbrInfo = item;
                this.$nextTick(async () => {
                    this.$bvModal.show('modal_mbrInfo');
                });
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

                    const {data} = await this.$axios.post(process.env.contextPath + '/mbr/deleteMbr',{
                        mbrIdList : deleteMbrIdList
                    });

                    await this.getMbrList();
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            isAdmin() {
                return this.user.mbrId === 'admin';
            }
        }

    }
</script>

<style scoped>

</style>
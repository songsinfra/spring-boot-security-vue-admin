<template>
    <b-modal id="modal_update_up_menu"
             centered
             @ok.prevent="handleOk"
             ref="modal"
             @show="showModal"
             title="메뉴 수정">
        <b-form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group
                    id="upMenuName-group-1"
                    label="상위메뉴명:"
                    label-for="upMenuName-1"
            >
                <b-form-input
                        id="upMenuName-1"
                        v-model="upMenu.upMenuName"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="사용여부:"
                    label-for="useYn-1"
            >
                <b-form-select id="useYn-1" :required="true"
                               v-model="upMenu.useYn" :options="useYnOption"
                ></b-form-select>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        props: ['selectedMenu', 'state'],
        name: "newUpMenu",
        data() {
            return {
                upMenu: {}
            };
        },
        computed: {
            useYnOption() {
                return this.$store.state.menu.useYnList;
            }
        },
        watch: {
            selectedMenu() {
                if(this.$props.state === 'CREATE') this.upMenu = {};
            }
        },
        methods:{
           async handleOk() {
               debugger;
               console.dir(this.$refs.form);
               const result = this.$refs.form.checkValidity();
               if(!result) {
                   await this.$bvModal.msgBoxOk("form 값이 유효하지 않습니다.");
                   return;
               }

               this.handleSubmit();
            },
            async handleSubmit() {
                try {
                    const url = this.$props.state === 'CREATE' ? "/api/menu/setUpMenuAdd" : "/api/menu/setMenuUpdate";
                    const response = await this.$axios.$post(url, {
                        menuId: this.upMenu.menuId,
                        menuName: this.upMenu.upMenuName,
                        menuURL: this.upMenu.menuURL,
                        ordNo: this.upMenu.ordNo,
                        useYn: this.upMenu.useYn
                    });

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshGrid');
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }
            },
            async showModal() {
                try {
                    this.$nextTick(async () => {
                        if (!this.$props.selectedMenu.menuId) return;

                        this.upMenu = {...this.$props.selectedMenu};
                    });
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>
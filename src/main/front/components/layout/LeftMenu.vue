<template>
    <div class="main-menu menu-fixed menu-light menu-accordion menu-shadow " data-scroll-to-active="true"
         data-img="/images/backgrounds/02.jpg">
        <div class="navbar-header">
            <ul class="nav navbar-nav flex-row">
                <li class="nav-item mr-auto"><a class="navbar-brand" href="index.html"><img class="brand-logo"
                                                                                            alt="Chameleon admin logo"
                                                                                            src="/images/logo/logo.png"/>
                    <h3 class="brand-text">Chameleon</h3></a></li>
                <li class="nav-item d-md-none"><a class="nav-link close-navbar"><i class="ft-x"></i></a></li>
            </ul>
        </div>
        <div class="main-menu-content">
            <ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">
                <li class=" nav-item"><a href="#"><i class="ft-home"></i><span class="menu-title" data-i18n="">Dashboard</span><span
                        class="badge badge badge-info badge-pill float-right mr-2">3</span></a>
                    <ul class="menu-content">
                        <li><a class="menu-item" href="dashboard-ecommerce.html">eCommerce</a>
                        </li>
                        <li class="active"><a class="menu-item" href="dashboard-analytics.html">Analytics</a>
                        </li>
                    </ul>
                </li>
                <li class=" nav-item"><a href="#"><i class="ft-layers"></i><span class="menu-title" data-i18n="">Apps</span></a>
                    <ul class="menu-content">
                        <li><a class="menu-item" href="email-application.html">Email Application</a>
                        </li>
                        <li><a class="menu-item" href="chat-application.html">Chat Application</a>
                        </li>
                        <li><a class="menu-item" href="full-calender.html">Full Calendar</a>
                        </li>
                        <li><a class="menu-item" href="project-summary.html">Project Summary</a>
                        </li>
                        <li><a class="menu-item" href="invoice-template.html">Invoice</a>
                        </li>
                        <li><a class="menu-item" href="#">Timelines</a>
                            <ul class="menu-content">
                                <li><a class="menu-item" href="timeline-center.html">Timelines Center</a>
                                </li>
                                <li><a class="menu-item" href="timeline-horizontal.html">Timelines Horizontal</a>
                                </li>
                            </ul>
                        </li>
                        <li><a class="menu-item" href="user-profile.html">Users Profile</a>
                        </li>
                        <li><a class="menu-item" href="users-contacts.html">Contact List</a>
                        </li>
                    </ul>
                </li>
                <li class=" nav-item"><a href="https://themeselection.com/demo/chameleon-admin-template/documentation"><i
                        class="ft-book"></i><span class="menu-title" data-i18n="">Documentation</span></a>
                </li>
            </ul>
        </div>
        <div class="navigation-background"></div>
    </div>
</template>

<script>
    export default {
        name: "leftMenu",
        methods:{
            goMenu({redirect, store}, menuId) {

                return redirect(menuId);
            }
        }
    }
</script>

<style scoped>

</style>
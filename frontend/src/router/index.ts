import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'
import { simulatorHandler, loginExistHandler, loginAccecptHandler, blockedIpHandler } from './guards'
import routes from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

router.beforeEach(async (to, from, next) => {
  const guardArr: {
    (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext): Promise<boolean> | boolean
  }[] = []

  // guardArr.push(blockedIpHandler)
  guardArr.push(simulatorHandler)

  if (!to.path.toLowerCase().startsWith('/blocked')) {
    console.log('not blocked page')
    guardArr.push(blockedIpHandler)
    if (!(to.path.toLowerCase().startsWith('/login') || to.path.toLowerCase().startsWith('/signup'))) {
      guardArr.push(loginExistHandler)
    } else guardArr.push(loginAccecptHandler)
  } else {
    console.log('blocked page')
  }

  let flag = true
  for (const guard of guardArr) {
    if (!(await guard(to, from, next))) {
      flag = false
      break
    }
  }

  if (flag) next()
})
export default router

import { Navigate, Route, Routes } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute'
import Layout from './components/Layout'
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import PendingApprovalPage from './pages/auth/PendingApprovalPage'
import DashboardPage from './pages/DashboardPage'
import ProfilePage from './pages/profile/ProfilePage'
import FindItPage from './pages/findit/FindItPage'
import CreateFindItPage from './pages/findit/CreateFindItPage'
import FindItDetailsPage from './pages/findit/FindItDetailsPage'
import MyFindItPage from './pages/findit/MyFindItPage'
import BorrowHubPage from './pages/borrowhub/BorrowHubPage'
import CreateBorrowRequestPage from './pages/borrowhub/CreateBorrowRequestPage'
import BorrowRequestDetailsPage from './pages/borrowhub/BorrowRequestDetailsPage'
import ComplaintsPage from './pages/complaints/ComplaintsPage'
import CreateComplaintPage from './pages/complaints/CreateComplaintPage'
import ComplaintDetailsPage from './pages/complaints/ComplaintDetailsPage'
import TalentHubPage from './pages/talent/TalentHubPage'
import CreateTalentPostPage from './pages/talent/CreateTalentPostPage'
import TalentDetailsPage from './pages/talent/TalentDetailsPage'
import NotificationsPage from './pages/NotificationsPage'
import AdminDashboardPage from './pages/admin/AdminDashboardPage'
import AdminVerificationPage from './pages/admin/AdminVerificationPage'
import AdminFindItClaimsPage from './pages/admin/AdminFindItClaimsPage'
import AdminComplaintsPage from './pages/admin/AdminComplaintsPage'
import AdminTalentPage from './pages/admin/AdminTalentPage'

export default function App(){return <Routes>
  <Route path="/" element={<LandingPage/>}/>
  <Route path="/login" element={<LoginPage/>}/>
  <Route path="/register" element={<RegisterPage/>}/>
  <Route path="/pending" element={<PendingApprovalPage/>}/>

  <Route element={<ProtectedRoute/>}><Route element={<Layout/>}>
    <Route element={<ProtectedRoute roles={['STUDENT','ALUMNI']}/>}>
      <Route path="/dashboard" element={<DashboardPage/>}/>
      <Route path="/notifications" element={<NotificationsPage/>}/>
      <Route path="/findit" element={<FindItPage/>}/><Route path="/findit/new" element={<CreateFindItPage/>}/><Route path="/findit/my" element={<MyFindItPage/>}/><Route path="/findit/:id" element={<FindItDetailsPage/>}/>
      <Route path="/borrowhub" element={<BorrowHubPage/>}/><Route path="/borrowhub/new" element={<CreateBorrowRequestPage/>}/><Route path="/borrowhub/:id" element={<BorrowRequestDetailsPage/>}/>
      <Route path="/complaints" element={<ComplaintsPage/>}/><Route path="/complaints/new" element={<CreateComplaintPage/>}/><Route path="/complaints/:id" element={<ComplaintDetailsPage/>}/>
      <Route path="/talent" element={<TalentHubPage/>}/><Route path="/talent/new" element={<CreateTalentPostPage/>}/><Route path="/talent/:id" element={<TalentDetailsPage/>}/>
    </Route>
    <Route path="/profile" element={<ProfilePage/>}/>
    <Route element={<ProtectedRoute roles={['ADMIN']}/>}>
      <Route path="/admin" element={<AdminDashboardPage/>}/><Route path="/admin/verification" element={<AdminVerificationPage/>}/><Route path="/admin/findit-claims" element={<AdminFindItClaimsPage/>}/><Route path="/admin/complaints" element={<AdminComplaintsPage/>}/><Route path="/admin/talent" element={<AdminTalentPage/>}/>
    </Route>
  </Route></Route>
  <Route path="*" element={<Navigate to="/" replace/>}/>
</Routes>}

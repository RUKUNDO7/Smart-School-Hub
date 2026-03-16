import { useEffect, useMemo, useState } from 'react'
import { apiFetch, getToken, login, register, setToken } from './api'

const roleLabels = ['ADMIN', 'TEACHER', 'PARENT', 'STUDENT']

function getValue(obj, path) {
  if (!obj || !path) return ''
  return path.split('.').reduce((acc, key) => (acc ? acc[key] : undefined), obj) ?? ''
}

function csvToNumberArray(value) {
  if (!value) return []
  return value
    .split(',')
    .map(v => v.trim())
    .filter(v => v.length)
    .map(v => Number(v))
}

function EntityPage({ title, endpoint, columns, fields }) {
  const [items, setItems] = useState([])
  const [form, setForm] = useState(() => {
    const init = {}
    fields.forEach(f => (init[f.name] = ''))
    return init
  })
  const [error, setError] = useState('')

  const load = async () => {
    setError('')
    try {
      const data = await apiFetch(endpoint)
      setItems(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    try {
      const payload = {}
      fields.forEach(f => {
        if (form[f.name] !== '') {
          if (typeof f.transform === 'function') {
            payload[f.name] = f.transform(form[f.name])
          } else if (f.type === 'number') {
            payload[f.name] = Number(form[f.name])
          } else {
            payload[f.name] = form[f.name]
          }
        }
      })
      await apiFetch(endpoint, { method: 'POST', body: JSON.stringify(payload) })
      setForm(fields.reduce((acc, f) => ({ ...acc, [f.name]: '' }), {}))
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <h2>{title}</h2>
        <button className="ghost" onClick={load}>Refresh</button>
      </div>
      {error && <div className="error">{error}</div>}
      <div className="grid">
        <div className="card">
          <h3>Create</h3>
          <form onSubmit={handleSubmit} className="form">
            {fields.map(field => (
              <label key={field.name} className="field">
                <span>{field.label}</span>
                <input
                  type={field.type || 'text'}
                  value={form[field.name]}
                  onChange={e => setForm({ ...form, [field.name]: e.target.value })}
                  placeholder={field.placeholder || ''}
                />
              </label>
            ))}
            <button type="submit">Create</button>
          </form>
        </div>
        <div className="card">
          <h3>List</h3>
          <div className="table">
            <div className="row head">
              {columns.map(col => (
                <div key={col.key} className="cell">{col.label}</div>
              ))}
            </div>
            {items.map(item => (
              <div key={item.id} className="row">
                {columns.map(col => (
                  <div key={col.key} className="cell">{String(getValue(item, col.key) ?? '')}</div>
                ))}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

function Dashboard() {
  const [data, setData] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    apiFetch('/dashboard/summary')
      .then(setData)
      .catch(err => setError(err.message))
  }, [])

  return (
    <div className="page">
      <div className="page-header">
        <h2>Dashboard</h2>
      </div>
      {error && <div className="error">{error}</div>}
      {data && (
        <div className="stats">
          {Object.entries(data).map(([key, value]) => (
            <div key={key} className="stat">
              <span className="stat-label">{key}</span>
              <span className="stat-value">{value}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function Reports() {
  const [reports, setReports] = useState({})
  const [error, setError] = useState('')

  useEffect(() => {
    const endpoints = ['students', 'classes', 'finances', 'attendance', 'teachers']
    Promise.all(endpoints.map(e => apiFetch(`/reports/${e}`)))
      .then(values => {
        const next = {}
        endpoints.forEach((key, i) => (next[key] = values[i]))
        setReports(next)
      })
      .catch(err => setError(err.message))
  }, [])

  return (
    <div className="page">
      <div className="page-header">
        <h2>Reports</h2>
      </div>
      {error && <div className="error">{error}</div>}
      <div className="grid">
        {Object.entries(reports).map(([key, value]) => (
          <div key={key} className="card">
            <h3>{key}</h3>
            <pre className="code">{JSON.stringify(value, null, 2)}</pre>
          </div>
        ))}
      </div>
    </div>
  )
}

function ParentPortal() {
  const [studentId, setStudentId] = useState('')
  const [progress, setProgress] = useState(null)
  const [error, setError] = useState('')

  const load = async () => {
    setError('')
    try {
      const data = await apiFetch(`/parent-portal/students/${studentId}/progress`)
      setProgress(data)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <h2>Parent Portal</h2>
      </div>
      <div className="card">
        <label className="field">
          <span>Student ID</span>
          <input value={studentId} onChange={e => setStudentId(e.target.value)} />
        </label>
        <button onClick={load} disabled={!studentId}>Load Progress</button>
      </div>
      {error && <div className="error">{error}</div>}
      {progress && (
        <div className="card">
          <pre className="code">{JSON.stringify(progress, null, 2)}</pre>
        </div>
      )}
    </div>
  )
}

function Login({ onLogin }) {
  const [mode, setMode] = useState('login')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState('STUDENT')
  const [studentId, setStudentId] = useState('')
  const [teacherId, setTeacherId] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    try {
      if (mode === 'login') {
        const res = await login(username, password)
        onLogin(res)
        return
      }

      const payload = {
        username,
        password,
        role
      }

      if (studentId) payload.studentId = Number(studentId)
      if (teacherId) payload.teacherId = Number(teacherId)

      const res = await register(payload)
      onLogin(res)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="login">
      <div className="card">
        <h1>Smart School Hub</h1>
        <p className="muted">{mode === 'login' ? 'Sign in to continue.' : 'Create your account.'}</p>
        {error && <div className="error">{error}</div>}
        <form onSubmit={handleSubmit} className="form">
          <label className="field">
            <span>Username</span>
            <input value={username} onChange={e => setUsername(e.target.value)} />
          </label>
          <label className="field">
            <span>Password</span>
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
          </label>

          {mode === 'register' && (
            <>
              <label className="field">
                <span>Role</span>
                <select value={role} onChange={e => setRole(e.target.value)}>
                  {roleLabels.map(r => (
                    <option key={r} value={r}>{r}</option>
                  ))}
                </select>
              </label>
              <label className="field">
                <span>Student ID (optional)</span>
                <input value={studentId} onChange={e => setStudentId(e.target.value)} />
              </label>
              <label className="field">
                <span>Teacher ID (optional)</span>
                <input value={teacherId} onChange={e => setTeacherId(e.target.value)} />
              </label>
            </>
          )}

          <button type="submit">{mode === 'login' ? 'Login' : 'Register'}</button>
        </form>
        <button className="ghost" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
          {mode === 'login' ? 'Create account' : 'Back to login'}
        </button>
      </div>
    </div>
  )
}

export default function App() {
  const [token, setTokenState] = useState(getToken())
  const [role, setRole] = useState(localStorage.getItem('ssh_role') || '')
  const [view, setView] = useState('dashboard')

  const onLogin = res => {
    setToken(res.token)
    localStorage.setItem('ssh_role', res.role)
    localStorage.setItem('ssh_user', res.username)
    setTokenState(res.token)
    setRole(res.role)
  }

  const onLogout = () => {
    setToken(null)
    localStorage.removeItem('ssh_role')
    localStorage.removeItem('ssh_user')
    setTokenState(null)
    setRole('')
  }

  const menu = useMemo(() => {
    const items = [
      { key: 'dashboard', label: 'Dashboard' },
      { key: 'students', label: 'Students' },
      { key: 'teachers', label: 'Teachers' },
      { key: 'classes', label: 'Classes' },
      { key: 'subjects', label: 'Subjects' },
      { key: 'attendance', label: 'Attendance' },
      { key: 'teacherAttendance', label: 'Teacher Attendance' },
      { key: 'exams', label: 'Exams' },
      { key: 'grades', label: 'Grades' },
      { key: 'fees', label: 'Fees' },
      { key: 'timetable', label: 'Timetable' },
      { key: 'evaluations', label: 'Teacher Evaluations' },
      { key: 'reports', label: 'Reports' },
      { key: 'parent', label: 'Parent Portal' }
    ]

    if (role === 'PARENT' || role === 'STUDENT') {
      return items.filter(i => ['parent', 'subjects', 'students', 'dashboard'].includes(i.key))
    }

    if (role === 'TEACHER') {
      return items.filter(i => !['fees', 'reports', 'evaluations'].includes(i.key))
    }

    return items
  }, [role])

  if (!token) {
    return <Login onLogin={onLogin} />
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">
          <span>Smart School Hub</span>
          <small>{roleLabels.includes(role) ? role : 'USER'}</small>
        </div>
        <nav>
          {menu.map(item => (
            <button
              key={item.key}
              className={view === item.key ? 'active' : ''}
              onClick={() => setView(item.key)}
            >
              {item.label}
            </button>
          ))}
        </nav>
        <button className="logout" onClick={onLogout}>Logout</button>
      </aside>
      <main>
        {view === 'dashboard' && <Dashboard />}
        {view === 'students' && (
          <EntityPage
            title="Students"
            endpoint="/students"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'firstName', label: 'First Name' },
              { key: 'lastName', label: 'Last Name' },
              { key: 'email', label: 'Email' },
              { key: 'schoolClass.id', label: 'Class ID' }
            ]}
            fields={[
              { name: 'firstName', label: 'First Name' },
              { name: 'lastName', label: 'Last Name' },
              { name: 'dob', label: 'DOB', type: 'date' },
              { name: 'gender', label: 'Gender (MALE/FEMALE/OTHER)' },
              { name: 'classId', label: 'Class ID', type: 'number' },
              { name: 'email', label: 'Email' },
              { name: 'phone', label: 'Phone' },
              { name: 'address', label: 'Address' }
            ]}
          />
        )}
        {view === 'teachers' && (
          <EntityPage
            title="Teachers"
            endpoint="/teachers"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'firstName', label: 'First Name' },
              { key: 'lastName', label: 'Last Name' },
              { key: 'email', label: 'Email' }
            ]}
            fields={[
              { name: 'firstName', label: 'First Name' },
              { name: 'lastName', label: 'Last Name' },
              { name: 'email', label: 'Email' },
              { name: 'phone', label: 'Phone' },
              {
                name: 'subjectIds',
                label: 'Subject IDs (comma separated)',
                transform: csvToNumberArray
              },
              {
                name: 'classIds',
                label: 'Class IDs (comma separated)',
                transform: csvToNumberArray
              }
            ]}
          />
        )}
        {view === 'classes' && (
          <EntityPage
            title="Classes"
            endpoint="/classes"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'name', label: 'Name' },
              { key: 'gradeLevel', label: 'Grade' },
              { key: 'teacher.id', label: 'Teacher ID' }
            ]}
            fields={[
              { name: 'name', label: 'Name' },
              { name: 'gradeLevel', label: 'Grade Level' },
              { name: 'teacherId', label: 'Teacher ID', type: 'number' }
            ]}
          />
        )}
        {view === 'subjects' && (
          <EntityPage
            title="Subjects"
            endpoint="/subjects"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'name', label: 'Name' },
              { key: 'schoolClass.id', label: 'Class ID' },
              { key: 'teacher.id', label: 'Teacher ID' }
            ]}
            fields={[
              { name: 'name', label: 'Name' },
              { name: 'classId', label: 'Class ID', type: 'number' },
              { name: 'teacherId', label: 'Teacher ID', type: 'number' }
            ]}
          />
        )}
        {view === 'attendance' && (
          <EntityPage
            title="Attendance"
            endpoint="/attendance"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'student.id', label: 'Student ID' },
              { key: 'date', label: 'Date' },
              { key: 'status', label: 'Status' }
            ]}
            fields={[
              { name: 'studentId', label: 'Student ID', type: 'number' },
              { name: 'date', label: 'Date', type: 'date' },
              { name: 'status', label: 'Status (PRESENT/ABSENT/LATE/EXCUSED)' }
            ]}
          />
        )}
        {view === 'teacherAttendance' && (
          <EntityPage
            title="Teacher Attendance"
            endpoint="/teacher-attendance"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'teacher.id', label: 'Teacher ID' },
              { key: 'date', label: 'Date' },
              { key: 'status', label: 'Status' }
            ]}
            fields={[
              { name: 'teacherId', label: 'Teacher ID', type: 'number' },
              { name: 'date', label: 'Date', type: 'date' },
              { name: 'status', label: 'Status (PRESENT/ABSENT/LATE/EXCUSED)' }
            ]}
          />
        )}
        {view === 'exams' && (
          <EntityPage
            title="Exams"
            endpoint="/exams"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'schoolClass.id', label: 'Class ID' },
              { key: 'subject.id', label: 'Subject ID' },
              { key: 'date', label: 'Date' }
            ]}
            fields={[
              { name: 'classId', label: 'Class ID', type: 'number' },
              { name: 'subjectId', label: 'Subject ID', type: 'number' },
              { name: 'date', label: 'Date', type: 'date' }
            ]}
          />
        )}
        {view === 'grades' && (
          <EntityPage
            title="Grades"
            endpoint="/grades"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'student.id', label: 'Student ID' },
              { key: 'exam.id', label: 'Exam ID' },
              { key: 'marks', label: 'Marks' }
            ]}
            fields={[
              { name: 'studentId', label: 'Student ID', type: 'number' },
              { name: 'examId', label: 'Exam ID', type: 'number' },
              { name: 'marks', label: 'Marks', type: 'number' }
            ]}
          />
        )}
        {view === 'fees' && (
          <EntityPage
            title="Fees"
            endpoint="/fees"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'student.id', label: 'Student ID' },
              { key: 'amountDue', label: 'Due' },
              { key: 'amountPaid', label: 'Paid' },
              { key: 'paymentStatus', label: 'Status' }
            ]}
            fields={[
              { name: 'studentId', label: 'Student ID', type: 'number' },
              { name: 'amountDue', label: 'Amount Due', type: 'number' },
              { name: 'amountPaid', label: 'Amount Paid', type: 'number' },
              { name: 'dueDate', label: 'Due Date', type: 'date' },
              { name: 'paymentStatus', label: 'Status (PENDING/PARTIAL/PAID/OVERDUE)' }
            ]}
          />
        )}
        {view === 'timetable' && (
          <EntityPage
            title="Timetable"
            endpoint="/timetable"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'schoolClass.id', label: 'Class ID' },
              { key: 'subject.id', label: 'Subject ID' },
              { key: 'teacher.id', label: 'Teacher ID' },
              { key: 'dayOfWeek', label: 'Day' },
              { key: 'startTime', label: 'Start' },
              { key: 'endTime', label: 'End' }
            ]}
            fields={[
              { name: 'classId', label: 'Class ID', type: 'number' },
              { name: 'subjectId', label: 'Subject ID', type: 'number' },
              { name: 'teacherId', label: 'Teacher ID', type: 'number' },
              { name: 'dayOfWeek', label: 'Day (MONDAY..SUNDAY)' },
              { name: 'startTime', label: 'Start (HH:mm)' },
              { name: 'endTime', label: 'End (HH:mm)' }
            ]}
          />
        )}
        {view === 'evaluations' && (
          <EntityPage
            title="Teacher Evaluations"
            endpoint="/teacher-evaluations"
            columns={[
              { key: 'id', label: 'ID' },
              { key: 'teacher.id', label: 'Teacher ID' },
              { key: 'date', label: 'Date' },
              { key: 'score', label: 'Score' }
            ]}
            fields={[
              { name: 'teacherId', label: 'Teacher ID', type: 'number' },
              { name: 'date', label: 'Date', type: 'date' },
              { name: 'score', label: 'Score', type: 'number' },
              { name: 'notes', label: 'Notes' }
            ]}
          />
        )}
        {view === 'reports' && <Reports />}
        {view === 'parent' && <ParentPortal />}
      </main>
    </div>
  )
}

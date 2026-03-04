import { useState, useEffect } from 'react';
import PageHeader from '../../components/ui/PageHeader';
import Card from '../../components/ui/Card';
import api from '../../config/api';
import { HiOutlineChevronLeft, HiOutlineChevronRight, HiOutlineCalendar } from 'react-icons/hi';

const CalendarPage = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const res = await api.get('/calendar/events').catch(() => ({ data: [] }));
        setEvents(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December',
  ];
  const dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
  const firstDayOfMonth = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  const prevMonth = () => setCurrentDate(new Date(year, month - 1, 1));
  const nextMonth = () => setCurrentDate(new Date(year, month + 1, 1));

  const today = new Date();
  const isToday = (day) => day === today.getDate() && month === today.getMonth() && year === today.getFullYear();

  // Filter events for current month and convert to calendar format
  const monthEvents = events
    .filter(e => {
      const eventDate = new Date(e.date);
      return eventDate.getMonth() === month && eventDate.getFullYear() === year;
    })
    .map(e => ({
      date: new Date(e.date).getDate(),
      title: e.title,
      color: e.type === 'exam' ? 'bg-error' : 
             e.type === 'deadline' ? 'bg-warning' :
             e.type === 'lecture' ? 'bg-success' : 'bg-brand-primary'
    }));

  const getEventsForDay = (day) => monthEvents.filter((e) => e.date === day);

  // Get upcoming events (future events sorted by date)
  const upcomingEvents = events
    .filter(e => new Date(e.date) >= new Date())
    .sort((a, b) => new Date(a.date) - new Date(b.date))
    .slice(0, 5);

  const typeColors = { exam: 'bg-error', event: 'bg-brand-primary', deadline: 'bg-warning', lecture: 'bg-success' };

  return (
    <div className="space-y-6">
      <PageHeader title="Calendar" subtitle="View upcoming events and deadlines" />

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Calendar */}
        <div className="lg:col-span-2">
          <Card>
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-lg font-bold text-navy-800">
                {monthNames[month]} {year}
              </h2>
              <div className="flex gap-2">
                <button
                  onClick={prevMonth}
                  className="p-2 rounded-xl hover:bg-surface-hover text-navy-400 transition-colors"
                >
                  <HiOutlineChevronLeft className="w-5 h-5" />
                </button>
                <button
                  onClick={nextMonth}
                  className="p-2 rounded-xl hover:bg-surface-hover text-navy-400 transition-colors"
                >
                  <HiOutlineChevronRight className="w-5 h-5" />
                </button>
              </div>
            </div>

            {/* Day headers */}
            <div className="grid grid-cols-7 gap-1 mb-2">
              {dayNames.map((day) => (
                <div key={day} className="text-center text-xs font-semibold text-text-muted py-2">
                  {day}
                </div>
              ))}
            </div>

            {/* Calendar grid */}
            <div className="grid grid-cols-7 gap-1">
              {/* Empty cells before first day */}
              {Array.from({ length: firstDayOfMonth }).map((_, i) => (
                <div key={`empty-${i}`} className="h-20 rounded-xl" />
              ))}

              {/* Day cells */}
              {Array.from({ length: daysInMonth }).map((_, i) => {
                const day = i + 1;
                const dayEvents = getEventsForDay(day);
                return (
                  <div
                    key={day}
                    className={`h-20 rounded-xl p-1.5 text-sm border transition-colors cursor-pointer hover:bg-surface-hover ${
                      isToday(day)
                        ? 'bg-brand-light border-brand-primary'
                        : 'border-transparent'
                    }`}
                  >
                    <span
                      className={`inline-flex items-center justify-center w-7 h-7 rounded-lg text-xs font-medium ${
                        isToday(day) ? 'bg-brand-primary text-white' : 'text-navy-700'
                      }`}
                    >
                      {day}
                    </span>
                    <div className="mt-0.5 space-y-0.5">
                      {dayEvents.map((e, idx) => (
                        <div
                          key={idx}
                          className={`${e.color} text-white text-[10px] px-1 py-0.5 rounded truncate`}
                        >
                          {e.title}
                        </div>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          </Card>
        </div>

        {/* Upcoming events */}
        <Card>
          <h2 className="text-lg font-semibold text-navy-800 mb-4">Upcoming Events</h2>
          {upcomingEvents.length > 0 ? (
            <div className="space-y-4">
              {upcomingEvents.map((event) => (
                <div key={event.id} className="flex gap-3 p-3 rounded-xl bg-surface-bg border border-surface-border">
                  <div className={`w-1 rounded-full ${typeColors[event.type] || 'bg-brand-primary'} flex-shrink-0`} />
                  <div>
                    <p className="font-medium text-navy-800 text-sm">{event.title}</p>
                    <p className="text-xs text-text-muted mt-1">
                      {new Date(event.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                      {event.time && ` • ${event.time}`}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8">
              <HiOutlineCalendar className="mx-auto h-12 w-12 text-text-muted mb-3" />
              <p className="text-text-muted">No upcoming events</p>
              <p className="text-sm text-text-muted mt-1">Your calendar is clear</p>
            </div>
          )}
        </Card>
      </div>
    </div>
  );
};

export default CalendarPage;

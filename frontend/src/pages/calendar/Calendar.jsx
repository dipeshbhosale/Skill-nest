import { useState } from 'react';
import { format, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isToday, addMonths, subMonths } from 'date-fns';
import { HiOutlineChevronLeft, HiOutlineChevronRight, HiOutlinePlus } from 'react-icons/hi';
import Button from '../../components/ui/Button';

const Calendar = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [selectedDate, setSelectedDate] = useState(new Date());

  const events = [
    { id: 1, title: 'Spring Boot Basics', type: 'class', date: '2026-03-03', time: '10:00 AM', color: 'bg-brand-primary' },
    { id: 2, title: 'Trees & Graphs', type: 'class', date: '2026-03-03', time: '2:00 PM', color: 'bg-success' },
    { id: 3, title: 'React Hooks', type: 'class', date: '2026-03-04', time: '11:00 AM', color: 'bg-brand-primary' },
    { id: 4, title: 'REST API Assignment Due', type: 'assignment', date: '2026-03-05', time: '11:59 PM', color: 'bg-error' },
    { id: 5, title: 'Microservices', type: 'class', date: '2026-03-04', time: '3:00 PM', color: 'bg-warning' },
    { id: 6, title: 'BST Assignment Due', type: 'assignment', date: '2026-03-07', time: '11:59 PM', color: 'bg-error' },
    { id: 7, title: 'Spring Security', type: 'class', date: '2026-03-06', time: '10:00 AM', color: 'bg-brand-primary' },
    { id: 8, title: 'Graph Algorithms', type: 'class', date: '2026-03-10', time: '2:00 PM', color: 'bg-success' },
    { id: 9, title: 'Todo App Due', type: 'assignment', date: '2026-03-10', time: '11:59 PM', color: 'bg-error' },
    { id: 10, title: 'State Management', type: 'class', date: '2026-03-11', time: '11:00 AM', color: 'bg-brand-primary' },
  ];

  const monthStart = startOfMonth(currentDate);
  const monthEnd = endOfMonth(currentDate);
  const days = eachDayOfInterval({ start: monthStart, end: monthEnd });

  // Pad the start of the month to align with day of week
  const startDay = monthStart.getDay();
  const paddedDays = [...Array(startDay).fill(null), ...days];

  const getEventsForDate = (date) => {
    if (!date) return [];
    const dateStr = format(date, 'yyyy-MM-dd');
    return events.filter((e) => e.date === dateStr);
  };

  const selectedDateStr = format(selectedDate, 'yyyy-MM-dd');
  const selectedEvents = events.filter((e) => e.date === selectedDateStr);

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Calendar</h1>
          <p className="text-text-secondary mt-1">View your schedule and events</p>
        </div>
        <Button variant="primary" icon={HiOutlinePlus}>Add Event</Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Calendar Grid */}
        <div className="lg:col-span-2 bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          {/* Month Navigation */}
          <div className="flex items-center justify-between mb-6">
            <button
              onClick={() => setCurrentDate(subMonths(currentDate, 1))}
              className="p-2 rounded-xl text-text-secondary hover:text-white hover:bg-navy-700/50 transition-colors"
            >
              <HiOutlineChevronLeft className="w-5 h-5" />
            </button>
            <h2 className="text-lg font-semibold text-white">
              {format(currentDate, 'MMMM yyyy')}
            </h2>
            <button
              onClick={() => setCurrentDate(addMonths(currentDate, 1))}
              className="p-2 rounded-xl text-text-secondary hover:text-white hover:bg-navy-700/50 transition-colors"
            >
              <HiOutlineChevronRight className="w-5 h-5" />
            </button>
          </div>

          {/* Day Headers */}
          <div className="grid grid-cols-7 gap-1 mb-2">
            {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
              <div key={day} className="text-center text-xs font-medium text-text-muted py-2">
                {day}
              </div>
            ))}
          </div>

          {/* Calendar Days */}
          <div className="grid grid-cols-7 gap-1">
            {paddedDays.map((day, i) => {
              if (!day) return <div key={`empty-${i}`} className="h-20" />;
              const dayEvents = getEventsForDate(day);
              const isSelected = format(day, 'yyyy-MM-dd') === selectedDateStr;

              return (
                <button
                  key={i}
                  onClick={() => setSelectedDate(day)}
                  className={`h-20 p-1 rounded-xl text-left transition-all duration-200 group
                    ${isToday(day) ? 'bg-brand-primary/20 border border-brand-primary/40' : ''}
                    ${isSelected ? 'bg-navy-700 border border-brand-primary/60' : 'hover:bg-navy-700/50'}
                    ${!isSameMonth(day, currentDate) ? 'opacity-30' : ''}`}
                >
                  <span className={`text-xs font-medium block mb-1 px-1
                    ${isToday(day) ? 'text-brand-accent' : 'text-text-secondary group-hover:text-white'}`}>
                    {format(day, 'd')}
                  </span>
                  <div className="space-y-0.5">
                    {dayEvents.slice(0, 2).map((evt) => (
                      <div key={evt.id} className={`${evt.color} rounded px-1 py-0.5`}>
                        <span className="text-[10px] text-white truncate block">{evt.title}</span>
                      </div>
                    ))}
                    {dayEvents.length > 2 && (
                      <span className="text-[10px] text-text-muted px-1">+{dayEvents.length - 2} more</span>
                    )}
                  </div>
                </button>
              );
            })}
          </div>
        </div>

        {/* Selected Day Events */}
        <div className="bg-surface-card rounded-2xl p-6 border border-navy-600/20">
          <h2 className="text-lg font-semibold text-white mb-1">
            {format(selectedDate, 'EEEE')}
          </h2>
          <p className="text-sm text-text-muted mb-5">{format(selectedDate, 'MMMM d, yyyy')}</p>

          {selectedEvents.length > 0 ? (
            <div className="space-y-3">
              {selectedEvents.map((evt) => (
                <div key={evt.id} className="flex items-start gap-3 p-3 rounded-xl bg-navy-700/50 hover:bg-navy-700 transition-colors">
                  <div className={`w-1 h-full min-h-[40px] rounded-full ${evt.color}`} />
                  <div>
                    <p className="text-sm font-medium text-white">{evt.title}</p>
                    <p className="text-xs text-text-muted">{evt.time}</p>
                    <span className={`inline-block mt-1 px-2 py-0.5 rounded-full text-xs
                      ${evt.type === 'class' ? 'bg-brand-primary/20 text-brand-accent' : 'bg-error/20 text-error'}`}>
                      {evt.type === 'class' ? 'Class' : 'Assignment'}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8">
              <p className="text-text-muted">No events for this day</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Calendar;

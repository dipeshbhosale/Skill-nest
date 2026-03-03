const CourseCard = ({ course, onClick }) => {
  const progressPercent = course.progress || 0;

  return (
    <div
      onClick={onClick}
      className="bg-surface-card rounded-2xl overflow-hidden border border-navy-600/20 hover:border-brand-primary/40 transition-all duration-300 cursor-pointer group"
    >
      {/* Thumbnail */}
      <div className="relative h-40 bg-gradient-to-br from-brand-primary/20 to-brand-secondary/20 overflow-hidden">
        {course.thumbnail ? (
          <img src={course.thumbnail} alt={course.title} className="w-full h-full object-cover" />
        ) : (
          <div className="w-full h-full flex items-center justify-center">
            <span className="text-4xl">📚</span>
          </div>
        )}
        <div className="absolute top-3 right-3">
          <span className="px-2 py-1 rounded-full text-xs font-medium bg-brand-primary/80 text-white backdrop-blur-sm">
            {course.category || 'General'}
          </span>
        </div>
      </div>

      {/* Content */}
      <div className="p-5">
        <h3 className="text-base font-semibold text-white group-hover:text-brand-accent transition-colors line-clamp-1">
          {course.title}
        </h3>
        <p className="text-sm text-text-secondary mt-1 line-clamp-2">
          {course.description}
        </p>

        {/* Teacher info */}
        <div className="flex items-center gap-2 mt-3">
          <div className="w-6 h-6 rounded-full gradient-primary flex items-center justify-center text-xs text-white font-bold">
            {course.teacherName?.charAt(0) || 'T'}
          </div>
          <span className="text-xs text-text-muted">{course.teacherName || 'Teacher'}</span>
        </div>

        {/* Progress bar */}
        {progressPercent > 0 && (
          <div className="mt-4">
            <div className="flex justify-between text-xs mb-1">
              <span className="text-text-muted">Progress</span>
              <span className="text-brand-accent">{progressPercent}%</span>
            </div>
            <div className="w-full h-1.5 bg-navy-700 rounded-full overflow-hidden">
              <div
                className="h-full gradient-primary rounded-full transition-all duration-500"
                style={{ width: `${progressPercent}%` }}
              />
            </div>
          </div>
        )}

        {/* Stats */}
        <div className="flex items-center justify-between mt-4 pt-3 border-t border-navy-600/20">
          <span className="text-xs text-text-muted">
            {course.totalClasses || 0} Classes
          </span>
          <span className="text-xs text-text-muted">
            {course.enrolledStudents || 0} Students
          </span>
        </div>
      </div>
    </div>
  );
};

export default CourseCard;

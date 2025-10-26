package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseAttributeDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseCategoryDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.ExerciseDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutDTO
import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanSimpleDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.ContentMedia
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Exercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttribute
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeExercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseAttributeType
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseCategory
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.ExerciseMedia
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.UserWorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.Workout
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutExercise
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlan
import br.ufpr.tads.daily_iu_services.domain.entity.exercise.WorkoutPlanWorkout
import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper(imports = [ExerciseAttributeType::class])
abstract class ExerciseMapper {

    companion object{
        val INSTANCE: ExerciseMapper = Mappers.getMapper(ExerciseMapper::class.java)
    }

    @Mapping(target = "type", expression = "java(ExerciseAttributeType.Companion.fromLabel(attribute.getType()).getValue())")
    abstract fun exerciseAttributeToDTO(attribute: ExerciseAttribute): ExerciseAttributeDTO

    abstract fun exerciseCategoryToDTO(category: ExerciseCategory): ExerciseCategoryDTO

    @Named("mediasToDTO")
    fun mediasToDTO(entities: List<ExerciseMedia>) = entities.map { mediaToDTO(it) }

    @Mapping(target = "id", source = "media.id")
    @Mapping(target = "url", source = "media.url")
    @Mapping(target = "contentType", source = "media.contentType")
    @Mapping(target = "contentSize", source = "media.contentSize")
    @Mapping(target = "altText", source = "media.altText")
    @Mapping(target = "createdAt", source = "media.createdAt")
    abstract fun mediaToDTO(entity: ExerciseMedia): MediaDTO

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    abstract fun mediaDTOToEntity(dto: MediaDTO): Media

    @Named("categoryToString")
    fun categoryToString(category: ExerciseCategory) = category.name

    @Named("selectBenefits")
    fun selectBenefits(attributes: List<ExerciseAttributeExercise>): List<ExerciseAttributeDTO> {
        return attributes.filter { it.attribute.type == ExerciseAttributeType.BENEFIT.label }
            .map { exerciseAttributeToDTO(it.attribute) }
    }

    @Mapping(target = "media", source = "media", qualifiedByName = ["mediasToDTO"])
    @Mapping(target = "category", source = "category", qualifiedByName = ["categoryToString"])
    @Mapping(target = "benefits", source = "attributes", qualifiedByName = ["selectBenefits"])
    abstract fun exerciseEntityToDTO(entity: Exercise): ExerciseDTO

    @Named("workoutExercisesToDTOMap")
    fun workoutExercisesToDTOMap(entities: List<WorkoutExercise>): Map<Int, ExerciseDTO> {
        return entities.sortedBy { it.exerciseOrder }
            .associate { it.exerciseOrder to exerciseEntityToDTO(it.exercise) }
    }

    @Mapping(target = "exercises", expression = "java(workoutExercisesToDTOMap(entity.getExercises()))")
    abstract fun workoutEntityToDTO(entity: Workout): WorkoutDTO

    @Named("workoutsPlanToDTOMap")
    fun workoutsPlanToDTOMap(entities: List<WorkoutPlanWorkout>): Map<Int, WorkoutDTO> {
        return entities.sortedBy { it.workoutOrder }
            .associate { it.workoutOrder to workoutEntityToDTO(it.workout) }
    }

    @Mapping(target = "workouts", expression = "java(workoutsPlanToDTOMap(entity.getWorkouts()))")
    abstract fun workoutPlanEntityToDTO(entity: WorkoutPlan): WorkoutPlanDTO

    @Mapping(target = "plan", expression = "java(workoutPlanEntityToDTO(entity.getPlan()))")
    abstract fun userWorkoutPlanEntityToDTO(entity: UserWorkoutPlan): UserWorkoutPlanDTO

    @Mapping(target = "plan", expression = "java(entity.getPlan().getName())")
    @Mapping(target = "description", expression = "java(entity.getPlan().getDescription())")
    abstract fun userWorkoutPlanEntityToSimpleDTO(entity: UserWorkoutPlan): UserWorkoutPlanSimpleDTO
}
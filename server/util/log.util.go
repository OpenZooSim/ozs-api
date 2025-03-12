package util

import (
	"os"

	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"github.com/rs/zerolog/pkgerrors"
)

func SetupZeroLogger(logLevel zerolog.Level) {
	zerolog.TimeFieldFormat = zerolog.TimeFormatUnix
	zerolog.ErrorStackMarshaler = pkgerrors.MarshalStack
	zerolog.SetGlobalLevel(logLevel)
	if logLevel == zerolog.DebugLevel {
		log.Logger = log.Output(zerolog.ConsoleWriter{Out: os.Stdout})
	}
}

func LogErrorWithStackTrace(err error) {
	log.Error().Stack().Err(err).Msg("")
}

func LogError(err error) {
	log.Error().Err(err).Msg("")
}

func LogWarning(message string) {
	log.Warn().Msg(message)
}

func LogInfo(message string) {
	log.Info().Msg(message)
}

func LogDebug(message string) {
	log.Debug().Msg(message)
}

interface MonthLabel {
    label: string
    monthIndex: number
    year: number
    date: Date
}

const dateNow = new Date()

const MONTHS_ABREV = [
    'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun',
    'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'
]


export function getCurrentYear() {
    const currentYear = dateNow.getFullYear()
    return currentYear
}


export function getLastMonths(quantity = 6): MonthLabel[] {
    const result: MonthLabel[] = []

    for (let i = quantity - 1; i >= 0; i--) {
        const date = new Date(dateNow.getFullYear(), dateNow.getMonth() - i, 1)

        result.push({
            label: `${MONTHS_ABREV[date.getMonth()]}/${date.getFullYear()}`,
            monthIndex: date.getMonth(),
            year: date.getFullYear(),
            date
        })
    }

    return result
}